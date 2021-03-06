/**
 * Copyright © 2013 Instituto Superior Técnico
 *
 * This file is part of FenixEdu IST Parking.
 *
 * FenixEdu IST Parking is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu IST Parking is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu IST Parking.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.parking.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.fenixedu.bennu.core.domain.Bennu;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Interval;
import org.joda.time.YearMonthDay;

import pt.ist.fenixedu.parking.domain.ParkingGroup;
import pt.ist.fenixedu.parking.domain.ParkingParty;

public class ParkingCardSearchBean implements Serializable {

    private ParkingGroup parkingGroup;

    private ParkingGroup newParkingGroup;

    private List<ParkingParty> searchedParkingParties = new ArrayList<ParkingParty>();

    private List<ParkingParty> selectedParkingParties = new ArrayList<ParkingParty>();;

    private YearMonthDay actualEndDate;

    private DateTime renewalEndDate;

    private int groupMembersCount;

    private ParkingCardSearchPeriod parkingCardSearchPeriod;

    private ParkingCardUserState parkingCardUserState;

    private ParkingCardUserState parkingCardState;

    private String emailText;

    public enum ParkingCardSearchPeriod {
        ENDS_BEFORE_ONE_MONTH, ENDS_BEFORE_TWO_MONTHS, ENDED_UNTIL_ONE_MONTH_AGO, ENDED_UNTIL_SIX_MONTHS_AGO,
        ENDED_UNTIL_ONE_YEAR_AGO;

        public static int getDays(ParkingCardSearchPeriod parkingCardSearchPeriod) {
            switch (parkingCardSearchPeriod) {
            case ENDS_BEFORE_ONE_MONTH:
                return 30;
            case ENDS_BEFORE_TWO_MONTHS:
                return 60;
            case ENDED_UNTIL_ONE_MONTH_AGO:
                return 30;
            case ENDED_UNTIL_SIX_MONTHS_AGO:
                return 180;
            case ENDED_UNTIL_ONE_YEAR_AGO:
                return 365;
            default:
                return 0;
            }
        }
    }

    public enum ParkingCardUserState {
        ALL, ACTIVE, INACTIVE;
    }

    public ParkingCardSearchBean() {
        setParkingCardUserState(ParkingCardUserState.ALL);
        setParkingCardState(ParkingCardUserState.ALL);
    }

    public void doSearch() {
        getSearchedParkingParties().clear();
        for (ParkingParty parkingParty : Bennu.getInstance().getParkingPartiesSet()) {
            if (!parkingParty.getVehiclesSet().isEmpty() && satisfiesSearch(parkingParty)) {
                getSearchedParkingParties().add(parkingParty);
            }
        }
    }

    private boolean satisfiesSearch(ParkingParty parkingParty) {
        return satisfiesGroup(parkingParty) && satisfiesEndDate(parkingParty) && satisfiesUserState(parkingParty)
                && satisfiesPeriodCriteria(parkingParty) && satisfiesCardState(parkingParty);
    }

    private boolean satisfiesCardState(ParkingParty parkingParty) {
        switch (getParkingCardState()) {
        case ALL:
            return Boolean.TRUE;
        case ACTIVE:
            if (parkingParty.getCardEndDate() == null) {
                return Boolean.TRUE;
            } else {
                return new Interval(parkingParty.getCardStartDate(), parkingParty.getCardEndDate()).contains(new DateTime());
            }
        default:
            if (parkingParty.getCardEndDate() == null) {
                return Boolean.FALSE;
            } else {
                return !new Interval(parkingParty.getCardStartDate(), parkingParty.getCardEndDate()).contains(new DateTime());
            }
        }
    }

    private boolean satisfiesPeriodCriteria(ParkingParty parkingParty) {
        if (getParkingCardSearchPeriod() == null) {
            return Boolean.TRUE;
        } else {
            if (parkingParty.getCardEndDate() == null) {
                return Boolean.FALSE;
            } else {
                YearMonthDay today = new YearMonthDay();
                if (!today.isAfter(parkingParty.getCardEndDate().toYearMonthDay())
                        && (getParkingCardSearchPeriod().equals(ParkingCardSearchPeriod.ENDS_BEFORE_ONE_MONTH) || getParkingCardSearchPeriod()
                                .equals(ParkingCardSearchPeriod.ENDS_BEFORE_TWO_MONTHS))) {
                    int daysBetween = Days.daysBetween(today, parkingParty.getCardEndDate().toYearMonthDay()).getDays();
                    return daysBetween <= ParkingCardSearchPeriod.getDays(getParkingCardSearchPeriod());
                } else if (!today.isBefore(parkingParty.getCardEndDate().toYearMonthDay())
                        && (getParkingCardSearchPeriod().equals(ParkingCardSearchPeriod.ENDED_UNTIL_ONE_MONTH_AGO)
                                || getParkingCardSearchPeriod().equals(ParkingCardSearchPeriod.ENDED_UNTIL_SIX_MONTHS_AGO) || getParkingCardSearchPeriod()
                                .equals(ParkingCardSearchPeriod.ENDED_UNTIL_ONE_YEAR_AGO))) {
                    int daysBetween = Days.daysBetween(today, parkingParty.getCardEndDate().toYearMonthDay()).getDays();
                    return daysBetween <= ParkingCardSearchPeriod.getDays(getParkingCardSearchPeriod());
                }
            }
        }
        return false;
    }

    private boolean satisfiesEndDate(ParkingParty parkingParty) {
        return getActualEndDate() == null
                || (parkingParty.getCardEndDate() != null && getActualEndDate().isEqual(
                        parkingParty.getCardEndDate().toYearMonthDay()));
    }

    private boolean satisfiesGroup(ParkingParty parkingParty) {
        if (getParkingGroup() == null) {
            return Boolean.TRUE;
        }
        return getParkingGroup() == parkingParty.getParkingGroup();
    }

    private boolean satisfiesUserState(ParkingParty parkingParty) {
        switch (getParkingCardUserState()) {
        case ALL:
            return Boolean.TRUE;
        case ACTIVE:
            return parkingParty.isActiveInHisGroup();
        default:
            return !parkingParty.isActiveInHisGroup();
        }
    }

    public ParkingGroup getParkingGroup() {
        return parkingGroup;
    }

    public void setParkingGroup(ParkingGroup parkingGroup) {
        this.parkingGroup = parkingGroup;
    }

    public ParkingGroup getNewParkingGroup() {
        return newParkingGroup;
    }

    public void setNewParkingGroup(ParkingGroup newParkingGroup) {
        this.newParkingGroup = newParkingGroup;
    }

    public YearMonthDay getActualEndDate() {
        return actualEndDate;
    }

    public void setActualEndDate(YearMonthDay actualEndDate) {
        this.actualEndDate = actualEndDate;
    }

    public DateTime getRenewalEndDate() {
        return renewalEndDate;
    }

    public void setRenewalEndDate(DateTime renewalEndDate) {
        this.renewalEndDate = renewalEndDate;
    }

    public List<ParkingParty> getSearchedParkingParties() {
        return searchedParkingParties;
    }

    public void setSearchedParkingParties(List<ParkingParty> searchedParkingParties) {
        this.searchedParkingParties = searchedParkingParties;
    }

    public int getGroupMembersCount() {
        return groupMembersCount;
    }

    public void setGroupMembersCount(int groupMembersCount) {
        this.groupMembersCount = groupMembersCount;
    }

    public void orderSearchedParkingParties() {
        Collections.sort(getSearchedParkingParties(),
                Comparator.comparing(ParkingParty::getPartyClassification).thenComparing(ParkingParty::getCardEndDateToCompare));
    }

    public void orderSelectedParkingParties() {
        Collections.sort(getSelectedParkingParties(),
                Comparator.comparing(ParkingParty::getPartyClassification).thenComparing(ParkingParty::getCardEndDateToCompare));
    }

    public ParkingCardSearchPeriod getParkingCardSearchPeriod() {
        return parkingCardSearchPeriod;
    }

    public void setParkingCardSearchPeriod(ParkingCardSearchPeriod parkingCardSearchPeriod) {
        this.parkingCardSearchPeriod = parkingCardSearchPeriod;
    }

    public ParkingCardUserState getParkingCardUserState() {
        return parkingCardUserState;
    }

    public void setParkingCardUserState(ParkingCardUserState parkingCardUserState) {
        this.parkingCardUserState = parkingCardUserState;
    }

    public List<ParkingParty> getSelectedParkingParties() {
        return selectedParkingParties;
    }

    public void setSelectedParkingParties(List<ParkingParty> selectedParkingParties) {
        this.selectedParkingParties = selectedParkingParties;
    }

    public void removeSelectedParkingParty(String parkingPartyIDToRemove) {
        for (ParkingParty parkingParty : getSelectedParkingParties()) {
            if (parkingParty.getExternalId().equals(parkingPartyIDToRemove)) {
                getSelectedParkingParties().remove(parkingParty);
                break;
            }
        }
    }

    public ParkingCardUserState getParkingCardState() {
        return parkingCardState;
    }

    public void setParkingCardState(ParkingCardUserState parkingCardState) {
        this.parkingCardState = parkingCardState;
    }

    public String getEmailText() {
        return emailText;
    }

    public void setEmailText(String emailText) {
        this.emailText = emailText;
    }
}
