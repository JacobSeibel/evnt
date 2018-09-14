package evntapi.rest.mapper;

import evntapi.domain.PositionedImage;

import java.util.List;

public interface PositionedImageMapper {
    List<PositionedImage> findAll();
    PositionedImage findByPk(int pk);
    void insert(PositionedImage positionedImage);
}
