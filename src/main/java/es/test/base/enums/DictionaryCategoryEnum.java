package es.test.base.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 旺旺小学酥
 * @Time 2018/3/29
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum DictionaryCategoryEnum {

    /**
     * 课程分类
     */
    CURRICULUM_CATEGORY(100100, "课程分类"),
    LESSON_MODALITY(100200, "课程类型");

    private final Integer value;
    private final String name;
}
