package br.com.taicout6.todolist.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/primeiraRota")
public class Controller {
  
  @GetMapping("/primeiroMetodo")
  public String primeiraMensagem() {
    return "Funcionou!";
  }
}
