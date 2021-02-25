package com.revature.models;

import javax.persistence.AttributeConverter;

public class StatusAttributeConverter implements AttributeConverter<ReimbursementStatus,Integer> {


    @Override
    public Integer convertToDatabaseColumn(ReimbursementStatus reimbursementStatus) {

        switch(reimbursementStatus){

            case PENDING:
                return 1;

            case APPROVED:
                return 2;

            case DENIED:
                return 3;

            case CLOSED:
                return 4;

                default:
        System.out.println("No enum match");
    }
        return null;
    }

    @Override
    public ReimbursementStatus convertToEntityAttribute(Integer integer) {

        switch(integer){

            case 1:
                return ReimbursementStatus.PENDING;

            case 2:
                return ReimbursementStatus.APPROVED;

            case 3:
                return ReimbursementStatus.DENIED;

            case 4:

                return ReimbursementStatus.CLOSED;

            default:
        System.out.println("No enum match");
    }

        return null;
    }
}
