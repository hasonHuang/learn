package com.hason.service;

import com.hason.entity.Bird;
import com.hason.exception.EntityNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Bird 业务类
 *
 * @author Huanghs
 * @since 2.0
 * @date 2017/9/4
 */
@Service
public class BirdService {

    /**
     * 模拟从数据库中查询，只有id=1才有数据，没有查询到时抛出异常
     */
    public Bird getBird(Long id) throws EntityNotFoundException {
        Bird bird = getBirdNoException(id);
        if (bird == null)
            throw new EntityNotFoundException(Bird.class, "id", id.toString());
        return bird;
    }

    /**
     * 模拟从数据库中查询，只有id=1才有数据
     */
    public Bird getBirdNoException(Long id) {
        if (id == 1) {
            return new Bird().setId(1L)
                    .setScientificName("鸟的学名")
                    .setMass(100D)
                    .setSpecie("鸟的种类")
                    .setLength(123);
        }
        return null;
    }

    /**
     * 模拟保存数据
     */
    public Bird createBird(Bird bird) {
        return bird;
    }
}
