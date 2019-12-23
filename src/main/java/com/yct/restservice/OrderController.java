package com.yct.restservice;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.mediatype.vnderrors.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
class OrderController {

  private final OrderRepository repository;
  private final OrderRepresentationModelAssembler assembler;

  OrderController(OrderRepository repository, OrderRepresentationModelAssembler assembler) {
    this.repository = repository;
    this.assembler = assembler;
  }

  @GetMapping("/orders")
  CollectionModel<EntityModel<Order>> all() {
    List<EntityModel<Order>> orders = repository.findAll().stream()
        .map(assembler::toModel)
        .collect(Collectors.toList());

    return new CollectionModel<>(orders, linkTo(methodOn(OrderController.class).all()).withSelfRel());
  }

  @GetMapping("/orders/{id}")
  EntityModel<Order> one(@PathVariable Long id) {

    Order order = repository.findById(id)
        .orElseThrow(() -> new OrderNotFoundException(id));

    return assembler.toModel(order);
  }

  @PostMapping("/orders")
  ResponseEntity<?> newOrder(@RequestBody Order order) {
    order.setStatus(Status.IN_PROGRESS);
    Order newOrder = repository.save(order);
    EntityModel<Order> orderEntityModel = assembler.toModel(repository.save(newOrder));

    return ResponseEntity.created(linkTo(methodOn(OrderController.class).one(newOrder.getId())).toUri())
        .body(orderEntityModel);
  }

  @DeleteMapping("/orders/{id}/cancel")
  public ResponseEntity<?> cancel(@PathVariable Long id) {
    Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));
    if(order.getStatus() == Status.IN_PROGRESS) {
      order.setStatus(Status.CANCELLED);
      return ResponseEntity.ok(assembler.toModel(repository.save(order)));
    }

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
              .body(new VndErrors.VndError("Method not allowed",
                  "You can't cancel an order that is in the " + order.getStatus() + " status"));
  }

  @PutMapping("/orders/{id}/complete")
  public ResponseEntity<?> complete(@PathVariable Long id) {
    Order order = repository.findById(id).orElseThrow(() -> new OrderNotFoundException(id));

    if(order.getStatus() == Status.IN_PROGRESS) {
      order.setStatus(Status.COMPLETED);
      return ResponseEntity.ok(assembler.toModel(repository.save(order)));
    }

    return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
        .body(new VndErrors.VndError("Method not allowed", "You can't complete an order that is in the " + order.getStatus() + " status"));
  }
}
