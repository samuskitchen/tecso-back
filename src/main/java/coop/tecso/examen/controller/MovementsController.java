package coop.tecso.examen.controller;

import coop.tecso.examen.exception.AppException;
import coop.tecso.examen.model.Movements;
import coop.tecso.examen.service.MovementsService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/movements")
public class MovementsController {

    private static final Logger logger = LogManager.getLogger(MovementsController.class);

    @Autowired
    private MovementsService movementsService;


    @PostMapping(path = "save")
    public ResponseEntity<Movements> saveMovements(@RequestBody Movements movements) {
        return movementsService.saveAllMovements(movements)
                .map(movement -> {
                    logger.info("Created new Movements: " + movement.toString());
                    return ResponseEntity.ok(movement);
                }).orElseThrow(() -> new AppException("Unexpected error during save movements. Please try again"));
    }

    @GetMapping(path = "find/account")
    public Page<Movements> findMovementByAccount(@RequestParam(value = "id") Long idAccount,
                                                 @RequestParam(value = "page", required = false, defaultValue = "0") int page,
                                                 @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        return movementsService.findMovementByAccount(idAccount, page, size);
    }
}
