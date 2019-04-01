package ru.javawebinar.topjava;

import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TestUtil {
    public static String getContent(MvcResult result) throws UnsupportedEncodingException {
        return result.getResponse().getContentAsString();
    }

    public static <T> T readFromJson(ResultActions action, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(action.andReturn()), clazz);
    }

    public static <T> T readFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValue(getContent(result), clazz);
    }

    public static <T> List<T> readListFromJsonMvcResult(MvcResult result, Class<T> clazz) throws UnsupportedEncodingException {
        return JsonUtil.readValues(getContent(result), clazz);
    }

    public static <T> void assertMatch(T actual, T expected, String... ignoreFields) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, ignoreFields);
    }

    public static <T> void assertMatch(Iterable<T> actual, T... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static <T> void assertMatch(Iterable<T> actual, Iterable<T> expected, String... ignoreFields) {
        assertThat(actual).usingElementComparatorIgnoringFields(ignoreFields).isEqualTo(expected);
    }

//    public static <T> ResultMatcher contentJson(T... expected) {
//        return result -> assertMatch(readListFromJsonMvcResult(result, T.class), Arrays.asList(expected));
//    }
//
//    public static <T> ResultMatcher contentJson(T expected) {
//        return result -> assertMatch(readFromJsonMvcResult(result, T.class), expected);
//    }
}
