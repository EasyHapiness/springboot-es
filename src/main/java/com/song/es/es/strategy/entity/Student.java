package com.song.es.es.strategy.entity;

import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @Desc
 * @Author
 * @Date 2019/12/25
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Student {
    private String type;
    private String name;
}
