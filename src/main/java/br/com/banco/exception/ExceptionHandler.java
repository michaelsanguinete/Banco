package br.com.banco.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
@AllArgsConstructor
public class ExceptionHandler {

    private final ModelMapper mapper;

    @org.springframework.web.bind.annotation.ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity trataError404(EntityNotFoundException exception){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID inexistente");
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity trataError400(MethodArgumentNotValidException exception){
        var erros = exception.getFieldErrors();
        List<DadosErroValidacao> listaErros = erros.stream()
                .map(DadosErroValidacao::new)
                .collect(Collectors.toList());
        return ResponseEntity.badRequest().body(listaErros);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(SaldoInsuficienteException.class)
    public ResponseEntity trataSaldoInsuficiente(SaldoInsuficienteException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
    }
}

@Data
@AllArgsConstructor
class DadosErroValidacao {

    private String campo;
    private String mensagem;

    public DadosErroValidacao(FieldError fieldError){
        this(fieldError.getField(), fieldError.getDefaultMessage());
    }
}
