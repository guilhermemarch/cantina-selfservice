package com.cantinasa.cantinasa.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Item_pedido {

   @Id
   @GenerationType.IDENTITY
   private Long idItem_pedido;
   private int   pedido_id;
   private Long  produto_id;
   private int  quantidade;

}
