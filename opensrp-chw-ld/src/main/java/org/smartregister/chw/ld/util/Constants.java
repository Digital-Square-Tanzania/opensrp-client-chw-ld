package org.smartregister.chw.ld.util;

public interface Constants {

    int REQUEST_CODE_GET_JSON = 2244;
    String ENCOUNTER_TYPE = "encounter_type";
    String STEP_ONE = "step1";
    String STEP_TWO = "step2";
    String LD_VISIT_GROUP = "ld_visit_group";

    interface JSON_FORM_EXTRA {
        String JSON = "json";
        String ENCOUNTER_TYPE = "encounter_type";
    }

    interface EVENT_TYPE {
        String LD_CONFIRMATION = "LD Confirmation";
        String LD_FOLLOW_UP_VISIT = "LD Follow-up Visit";
        String VOID_EVENT = "Void Event";
        String LD_GENERAL_EXAMINATION = "LD General Examination";
    }

    interface FORMS {
        String LD_REGISTRATION = "ld_registration";
        String LD_FOLLOW_UP_VISIT = "ld_followup_visit";
    }

    interface TABLES {
        String LD_CONFIRMATION = "ec_ld_confirmation";
        String LD_FOLLOW_UP = "ec_ld_follow_up_visit";
        String EC_LD_GENERAL_EXAMINATION = "ec_ld_general_examination_consultation";
        String EC_LD_PARTOGRAPH = "ec_ld_partograph";
    }

    interface ACTIVITY_PAYLOAD {
        String BASE_ENTITY_ID = "BASE_ENTITY_ID";
        String FAMILY_BASE_ENTITY_ID = "FAMILY_BASE_ENTITY_ID";
        String ACTION = "ACTION";
        String LD_FORM_NAME = "LD_FORM_NAME";
        String EDIT_MODE = "editMode";
        String MEMBER_PROFILE_OBJECT = "MemberObject";

    }

    interface ACTIVITY_PAYLOAD_TYPE {
        String REGISTRATION = "REGISTRATION";
        String FOLLOW_UP_VISIT = "FOLLOW_UP_VISIT";
    }

    interface CONFIGURATION {
        String LD_CONFIRMATION = "ld_confirmation";
    }

    interface LD_MEMBER_OBJECT {
        String MEMBER_OBJECT = "memberObject";
    }

}