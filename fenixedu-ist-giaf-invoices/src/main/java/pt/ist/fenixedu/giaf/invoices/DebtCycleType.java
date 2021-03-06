/**
 * Copyright © 2013 Instituto Superior Técnico
 *
 * This file is part of FenixEdu IST GIAF Invoices.
 *
 * FenixEdu IST GIAF Invoices is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu IST GIAF Invoices is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu IST GIAF Invoices.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.giaf.invoices;

import org.fenixedu.academic.domain.degreeStructure.CycleType;

public enum DebtCycleType {

    FIRST_CYCLE, SECOND_CYCLE, INTEGRATED_MASTER, THIRD_CYCLE;

    static DebtCycleType valueOf(CycleType cycleType) {
        switch (cycleType) {
        case FIRST_CYCLE:
            return FIRST_CYCLE;
        case SECOND_CYCLE:
            return SECOND_CYCLE;
        case THIRD_CYCLE:
            return THIRD_CYCLE;
        default:
            return null;
        }
    }

    public String getDescription() {
        switch (this) {
        case FIRST_CYCLE:
            return CycleType.FIRST_CYCLE.getDescription();
        case SECOND_CYCLE:
            return CycleType.SECOND_CYCLE.getDescription();
        case THIRD_CYCLE:
            return CycleType.THIRD_CYCLE.getDescription();
        default:
//            return CycleType.SECOND_CYCLE.getDescription();
//            return DegreeType.BOLONHA_INTEGRATED_MASTER_DEGREE.getLocalizedName();
            return "Mestrado Integrado";
        }
    }

}
