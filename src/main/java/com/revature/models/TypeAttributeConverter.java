package com.revature.models;

import javax.persistence.AttributeConverter;

public class TypeAttributeConverter implements AttributeConverter<ReimbursementType,Integer> {


    @Override
    public Integer convertToDatabaseColumn(ReimbursementType reimbursementType) {

        switch(reimbursementType){

            case LODGING:
                return 1;

            case TRAVEL:
                return 2;

            case FOOD:
                return 3;

            case OTHER:
                return 4;

            default:
        System.out.println("No enum match");
    }
        return null;
    }

    @Override
    public ReimbursementType convertToEntityAttribute(Integer integer) {

        switch(integer){
            case 1:
                return ReimbursementType.LODGING;


            case 2:
                return ReimbursementType.TRAVEL;

            case 3:

                return ReimbursementType.FOOD;

            case 4:
                return ReimbursementType.OTHER;

            default:
        System.out.println("No enum match");
    }
        return null;
    }
}
