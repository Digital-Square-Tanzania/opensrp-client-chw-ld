package org.smartregister.chw.ld.dao;

import org.smartregister.chw.ld.domain.PartographChartObject;
import org.smartregister.chw.ld.domain.MemberObject;
import org.smartregister.chw.ld.util.Constants;
import org.smartregister.dao.AbstractDao;
import org.smartregister.util.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import timber.log.Timber;

public class LDDao extends AbstractDao {

    public static Date getLDTestDate(String baseEntityID) {
        String sql = "select ld_test_date from " + Constants.TABLES.LD_CONFIRMATION + " where base_entity_id = '" + baseEntityID + "'";

        DataMap<Date> dataMap = cursor -> getCursorValueAsDate(cursor, "ld_test_date", getNativeFormsDateFormat());

        List<Date> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }

    public static Date getLDFollowUpVisitDate(String baseEntityID) {
        String sql = "SELECT eventDate FROM event where eventType ='LD Follow-up Visit' AND baseEntityId ='" + baseEntityID + "'";

        DataMap<Date> dataMap = cursor -> getCursorValueAsDate(cursor, "eventDate", getNativeFormsDateFormat());

        List<Date> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }

    public static void closeLDMemberFromRegister(String baseEntityID) {
        String sql = "update " + Constants.TABLES.LD_CONFIRMATION + " set is_closed = 1 where base_entity_id = '" + baseEntityID + "'";
        updateDB(sql);
    }

    public static boolean isRegisteredForLD(String baseEntityID) {
        String sql = "SELECT count(p.base_entity_id) count FROM " + Constants.TABLES.LD_CONFIRMATION + " p " +
                "WHERE p.base_entity_id = '" + baseEntityID + "' AND p.is_closed = 0 AND p.labour_confirmation  = 'true' " +
                "AND datetime('NOW') <= datetime(p.last_interacted_with/1000, 'unixepoch', 'localtime','+15 days')";

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return false;

        return res.get(0) > 0;
    }

    public static Integer getLDFamilyMembersCount(String familyBaseEntityId) {
        String sql = "SELECT count(emc.base_entity_id) count FROM " + Constants.TABLES.LD_CONFIRMATION + " emc " +
                "INNER Join ec_family_member fm on fm.base_entity_id = emc.base_entity_id " +
                "WHERE fm.relational_id = '" + familyBaseEntityId + "' AND fm.is_closed = 0 " +
                "AND emc.is_closed = 0 AND emc.ld = 1";

        DataMap<Integer> dataMap = cursor -> getCursorIntValue(cursor, "count");

        List<Integer> res = readData(sql, dataMap);
        if (res == null || res.size() == 0)
            return 0;
        return res.get(0);
    }

    public static MemberObject getMember(String baseEntityID) {
        String sql = "select m.base_entity_id , m.unique_id , m.relational_id , m.dob , m.first_name , m.middle_name , m.last_name , m.gender , m.phone_number , m.other_phone_number , f.first_name family_name ,f.primary_caregiver , f.family_head , f.village_town ,fh.first_name family_head_first_name , fh.middle_name family_head_middle_name , fh.last_name family_head_last_name, fh.phone_number family_head_phone_number , ancr.is_closed anc_is_closed, pncr.is_closed pnc_is_closed, pcg.first_name pcg_first_name , pcg.last_name pcg_last_name , pcg.middle_name pcg_middle_name , pcg.phone_number  pcg_phone_number , mr.* from ec_family_member m inner join ec_family f on m.relational_id = f.base_entity_id inner join " + Constants.TABLES.LD_CONFIRMATION + " mr on mr.base_entity_id = m.base_entity_id left join ec_family_member fh on fh.base_entity_id = f.family_head left join ec_family_member pcg on pcg.base_entity_id = f.primary_caregiver left join ec_anc_register ancr on ancr.base_entity_id = m.base_entity_id left join ec_pregnancy_outcome pncr on pncr.base_entity_id = m.base_entity_id where m.base_entity_id ='" + baseEntityID + "' AND mr.labour_confirmation = 'true'";
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

        DataMap<MemberObject> dataMap = cursor -> {
            MemberObject memberObject = new MemberObject();

            memberObject.setFirstName(getCursorValue(cursor, "first_name", ""));
            memberObject.setMiddleName(getCursorValue(cursor, "middle_name", ""));
            memberObject.setLastName(getCursorValue(cursor, "last_name", ""));
            memberObject.setAddress(getCursorValue(cursor, "village_town"));
            memberObject.setGender(getCursorValue(cursor, "gender"));
            memberObject.setUniqueId(getCursorValue(cursor, "unique_id", ""));
            memberObject.setAge(String.valueOf(Utils.getAgeFromDate(getCursorValue(cursor, "dob"))));
            memberObject.setFamilyBaseEntityId(getCursorValue(cursor, "relational_id", ""));
            memberObject.setRelationalId(getCursorValue(cursor, "relational_id", ""));
            memberObject.setPrimaryCareGiver(getCursorValue(cursor, "primary_caregiver"));
            memberObject.setFamilyName(getCursorValue(cursor, "family_name", ""));
            memberObject.setPhoneNumber(getCursorValue(cursor, "phone_number", ""));
            memberObject.setLDTestDate(getCursorValueAsDate(cursor, "ld_test_date", df));
            memberObject.setBaseEntityId(getCursorValue(cursor, "base_entity_id", ""));
            memberObject.setFamilyHead(getCursorValue(cursor, "family_head", ""));
            memberObject.setFamilyHeadPhoneNumber(getCursorValue(cursor, "pcg_phone_number", ""));
            memberObject.setFamilyHeadPhoneNumber(getCursorValue(cursor, "family_head_phone_number", ""));
            memberObject.setAncMember(getCursorValue(cursor, "anc_is_closed", ""));
            memberObject.setPncMember(getCursorValue(cursor, "pnc_is_closed", ""));

            String familyHeadName = getCursorValue(cursor, "family_head_first_name", "") + " "
                    + getCursorValue(cursor, "family_head_middle_name", "");

            familyHeadName =
                    (familyHeadName.trim() + " " + getCursorValue(cursor, "family_head_last_name", "")).trim();
            memberObject.setFamilyHeadName(familyHeadName);

            String familyPcgName = getCursorValue(cursor, "pcg_first_name", "") + " "
                    + getCursorValue(cursor, "pcg_middle_name", "");

            familyPcgName =
                    (familyPcgName.trim() + " " + getCursorValue(cursor, "pcg_last_name", "")).trim();
            memberObject.setPrimaryCareGiverName(familyPcgName);

            return memberObject;
        };

        List<MemberObject> res = readData(sql, dataMap);
        if (res == null || res.size() != 1)
            return null;

        return res.get(0);
    }


    public static String getLabourStage(String baseEntityId) {
        String sql = "SELECT labour_stage FROM " + Constants.TABLES.LD_CONFIRMATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "labour_stage");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static String getCervixDilation(String baseEntityId) {
        String sql = "SELECT cervix_dilation FROM " + Constants.TABLES.EC_LD_GENERAL_EXAMINATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "cervix_dilation");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static String getLabourOnsetDate(String baseEntityId) {
        String sql = "SELECT labour_onset_date FROM " + Constants.TABLES.LD_CONFIRMATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "labour_onset_date");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static String getLabourOnsetTime(String baseEntityId) {
        String sql = "SELECT labour_onset_time FROM " + Constants.TABLES.LD_CONFIRMATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "labour_onset_time");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static String getVaginalExaminationDate(String baseEntityId) {
        String sql = "SELECT vaginal_exam_date FROM " + Constants.TABLES.EC_LD_GENERAL_EXAMINATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "vaginal_exam_date");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static String getVaginalExaminationTime(String baseEntityId) {
        String sql = "SELECT vaginal_exam_time FROM " + Constants.TABLES.EC_LD_GENERAL_EXAMINATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "vaginal_exam_time");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static String getPartographDate(String baseEntityId) {
        String sql = "SELECT partograph_date FROM " + Constants.TABLES.EC_LD_PARTOGRAPH + " WHERE entity_id = '" + baseEntityId + "' ORDER BY last_interacted_with DESC LIMIT 1";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "partograph_date");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static String getPartographTime(String baseEntityId) {
        String sql = "SELECT partograph_time FROM " + Constants.TABLES.EC_LD_PARTOGRAPH + " WHERE entity_id = '" + baseEntityId + "' ORDER BY last_interacted_with DESC LIMIT 1";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "partograph_time");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }


    public static String getMembraneState(String baseEntityId) {
        String sql = "SELECT membrane FROM " + Constants.TABLES.LD_CONFIRMATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "membrane");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }


    public static String getMoulding(String baseEntityId) {
        String sql = "SELECT moulding FROM " + Constants.TABLES.LD_CONFIRMATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "moulding");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }


    public static String getDescent(String baseEntityId) {
        String sql = "SELECT descent_presenting_part FROM " + Constants.TABLES.LD_CONFIRMATION + " WHERE base_entity_id = '" + baseEntityId + "'";

        DataMap<String> dataMap = cursor -> getCursorValue(cursor, "descent_presenting_part");

        List<String> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static Long getPartographStartTime(String baseEntityId) {
        String sql = "SELECT partograph_date, partograph_time FROM " + Constants.TABLES.EC_LD_PARTOGRAPH + " WHERE entity_id = '" + baseEntityId + "' ORDER BY last_interacted_with LIMIT 1";

        DataMap<Long> dataMap = cursor -> {
            String partographDate = getCursorValue(cursor, "partograph_date", "");
            String partographTime = getCursorValue(cursor, "partograph_time", "");

            String concatText = partographDate + " " + partographTime;

            try {
                Date parseDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(concatText);
                return parseDate.getTime();
            } catch (ParseException e) {
                Timber.e(e);
            }
            return null;
        };

        List<Long> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res.get(0);
        return null;
    }

    public static List<PartographChartObject> getCervixDilationList(String baseEntityId) {
        String sql = "SELECT partograph_date, partograph_time, cervix_dilation FROM " + Constants.TABLES.EC_LD_PARTOGRAPH + " WHERE entity_id = '" + baseEntityId + "' AND cervix_dilation IS NOT NULL";

        DataMap<PartographChartObject> dataMap = cursor -> {
            String partographDate = getCursorValue(cursor, "partograph_date", "");
            String partographTime = getCursorValue(cursor, "partograph_time", "");

            String concatText = partographDate + " " + partographTime;

            try {
                Date parseDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(concatText);
                return new PartographChartObject(parseDate.getTime(), getCursorIntValue(cursor, "cervix_dilation", 0));
            } catch (ParseException e) {
                Timber.e(e);
            }
            return null;
        };

        List<PartographChartObject> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res;
        return null;
    }
    public static List<PartographChartObject> getDescentList(String baseEntityId) {
        String sql = "SELECT partograph_date, partograph_time, descent_presenting_part FROM " + Constants.TABLES.EC_LD_PARTOGRAPH + " WHERE entity_id = '" + baseEntityId + "' AND descent_presenting_part IS NOT NULL";

        DataMap<PartographChartObject> dataMap = cursor -> {
            String partographDate = getCursorValue(cursor, "partograph_date", "");
            String partographTime = getCursorValue(cursor, "partograph_time", "");

            String concatText = partographDate + " " + partographTime;

            try {
                Date parseDate = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault()).parse(concatText);
                return new PartographChartObject(parseDate.getTime(), getCursorIntValue(cursor, "descent_presenting_part", 5));
            } catch (ParseException e) {
                Timber.e(e);
            }
            return null;
        };

        List<PartographChartObject> res = readData(sql, dataMap);
        if (res != null && res.size() > 0)
            return res;
        return null;
    }
}
