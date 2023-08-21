package com.fairgoods.webshop.controller;

import com.fairgoods.webshop.dto.PositionDTO;
import com.fairgoods.webshop.model.Position;
import com.fairgoods.webshop.service.PositionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/positions")
@RequiredArgsConstructor
public class PositionController {

    private final PositionService positionService;

    @PostMapping
    @ResponseStatus(CREATED)
    public Position createPosition(@RequestBody @Valid PositionDTO positionDTO) {
        return positionService.save(fromDTO(positionDTO), 0L, positionDTO.getProductId());
    }

    private static Position fromDTO(PositionDTO positionDTO) {
        return new Position(positionDTO.getId(), positionDTO.getQuantity());
    }
}