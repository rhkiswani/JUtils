package com.rhkiswani.commons.beans.withIdAnnotation.withEqualsAnnotation;

public class EmployeeByIdAnnotation extends PersonByIdAnnotation<EmployeeByIdAnnotation> {

    private int empId;

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }
}
