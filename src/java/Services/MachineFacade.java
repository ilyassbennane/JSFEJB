/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services;

import Entities.Machine;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author PC
 */
@Stateless
public class MachineFacade extends AbstractFacade<Machine> {
    @PersistenceContext(unitName = "ControleIsicPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public MachineFacade() {
        super(Machine.class);
    }
    public Map<String, Integer> countMachinesParAnnee() {
    Map<String, Integer> machinesParAnnee = new LinkedHashMap<>(); // Utilisation de LinkedHashMap pour maintenir l'ordre d'insertion

        List<Object[]> results = em.createQuery("SELECT FUNCTION('YEAR', m.dateAchat), COUNT(m) FROM Machine m GROUP BY FUNCTION('YEAR', m.dateAchat) ORDER BY FUNCTION('YEAR', m.dateAchat) ASC")
                               .getResultList();

    for (Object[] result : results) {
        String annee = String.valueOf(result[0]);
        Integer nombreMachines = ((Number) result[1]).intValue();
        machinesParAnnee.put(annee, nombreMachines);
    }

    return machinesParAnnee;
}
    
}
