package coop.tecso.examen.controller;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.dto.MovementsRequest;
import coop.tecso.examen.service.MovementsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movements")
public class MovementsController {

    private static final Logger logger = LogManager.getLogger(MovementsController.class);

    @Autowired
    private MovementsService movementsService;


    @PostMapping(path = "save")
    public ResponseEntity<String> saveMovements(@RequestBody MovementsRequest movementsRequest) {
        return movementsService.saveAllMovements(movementsRequest)
                .map(movements -> {
                    logger.info("Created new Movements: " + movements.toString());
                    return ResponseEntity.ok(movements);
                }).orElseThrow(() -> new AppException("Unexpected error during save movements. Please try again"));
    }
}
