package coop.tecso.examen.model.dto;

import coop.tecso.examen.model.Movements;

import java.util.List;

public class MovementsRequest {

    private Long idAccount;

    private List<Movements> movements;

    public MovementsRequest() {
    }

    public MovementsRequest(Long idAccount, List<Movements> movements) {
        this.idAccount = idAccount;
        this.movements = movements;
    }

    public Long getIdAccount() {
        return idAccount;
    }

    public void setIdAccount(Long idAccount) {
        this.idAccount = idAccount;
    }

    public List<Movements> getMovements() {
        return movements;
    }

    public void setMovements(List<Movements> movements) {
        this.movements = movements;
    }
}
