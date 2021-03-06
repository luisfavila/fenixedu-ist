/**
 * Copyright © 2013 Instituto Superior Técnico
 *
 * This file is part of FenixEdu IST Tutorship.
 *
 * FenixEdu IST Tutorship is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu IST Tutorship is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu IST Tutorship.  If not, see <http://www.gnu.org/licenses/>.
 */
package pt.ist.fenixedu.tutorship.ui.Action.pedagogicalCouncil.studentLowPerformance;

import java.io.Serializable;

import org.fenixedu.academic.domain.ExecutionYear;

import pt.ist.fenixedu.tutorship.domain.PrescriptionEnum;

public class PrescriptionBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private PrescriptionEnum prescriptionEnum = PrescriptionEnum.MOMENT1;
    private ExecutionYear executionYear;

    public PrescriptionBean(PrescriptionEnum prescriptionEnum) {
        super();
        this.prescriptionEnum = prescriptionEnum;
    }

    public void setSelectedPrescriptionEnum(PrescriptionEnum prescriptionEnum) {
        this.prescriptionEnum = prescriptionEnum;
    }

    public ExecutionYear getExecutionYear() {
        return executionYear;
    }

    public void setExecutionYear(ExecutionYear executionYear) {
        this.executionYear = executionYear;
    }

    public PrescriptionEnum getSelectedPrescriptionEnum() {
        return prescriptionEnum;
    }
}
