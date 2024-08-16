package com.sivalabs.aidemo.ragdb;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;

@Profile("db")
// DatabaseService.java
@Service
public class DatabaseService {
    private final AccedantRepository accedantRepository;

    public DatabaseService(AccedantRepository accedantRepository/*, OrderRepository orderRepository*/) {
        this.accedantRepository = accedantRepository;
    }

    public String retrieveRelevantData() {
        StringBuilder relevantData = new StringBuilder();

        List<Accedant> accedants = accedantRepository.findAll();
//        List<Order> orders = orderRepository.findAll();

        relevantData.append("Voici la liste des accedants:\n");
        for (Accedant accedant  : accedants) {
            relevantData.append("accedant dont l'id est " + accedant.getId()).append(":  (")
                    .append("le nom est " + accedant.getNom()).append(", ")
                    .append("le prenom est " + accedant.getPrenom()).append(", ")
                    .append("l'email est " +accedant.getEmail()).append(")\n");
                    }

//        relevantData.append("\nOrders:\n");
//        for (Order order : orders) {
//            relevantData.append(order.getId()).append(": ")
//                    .append(order.getCustomer().getName()).append(" - ")
//                    .append(order.getProductName()).append(" - $")
//                    .append(order.getAmount()).append(" - ")
//                    .append(order.getOrderDate()).append("\n");
//        }

        return relevantData.toString();
    }
}