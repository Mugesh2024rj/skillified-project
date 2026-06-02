package com.todo.app;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/todos")
public class TodoController {
    private final List<TodoItem> todos = new ArrayList<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    @GetMapping
    public List<TodoItem> listTodos() {
        return new ArrayList<>(todos);
    }

    @PostMapping
    public TodoItem addTodo(@RequestBody TodoItem request) {
        if (request == null || request.getText() == null || request.getText().trim().isEmpty()) {
            throw new IllegalArgumentException("Task cannot be empty.");
        }
        TodoItem todo = new TodoItem(nextId.getAndIncrement(), request.getText().trim());
        todos.add(todo);
        return todo;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTodo(@PathVariable int id) {
        boolean removed = todos.removeIf(todo -> todo.getId() == id);
        return removed ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
