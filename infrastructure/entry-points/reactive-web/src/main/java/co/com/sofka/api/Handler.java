package co.com.sofka.api;

import co.com.sofka.model.Galleta;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class Handler {
    //private  final UseCase useCase;
    //private  final UseCase2 useCase2;

    public Mono<ServerResponse> listenGETUseCase(ServerRequest serverRequest) {
        return ServerResponse.ok().bodyValue("");
    }

    public Mono<ServerResponse> listenGETGalleta(ServerRequest serverRequest) {
        int iReq = Integer.parseInt(serverRequest.queryParam("iReq").orElse("-1"));
        return ServerResponse.ok().body(getGalleta(iReq), Mono.class);
    }

    private Flux<Galleta> getGalletas() {
        return Flux.fromIterable(Arrays.asList(
          new Galleta(1, "Ducales"),
          new Galleta(2, "Saltinas"),
          new Galleta(3, "Salricas"),
          new Galleta(4, "Chockis"),
          new Galleta(5, "Festival"),
          new Galleta(6, "Oreo"),
          new Galleta(7, "Cam"),
          new Galleta(8, "Dux"),
          new Galleta(9, "Club Social"),
          new Galleta(10, "Crakeñas")
        ));
    }

    private Mono<Galleta> getGalleta(int codigo) {
        Flux<Galleta> galletas = getGalletas();

        return galletas.filter(g -> g.getCodigo() == codigo)
                .reduce((subtotal, datag) -> {
                    subtotal.setCodigo(datag.getCodigo());
                    subtotal.setTipo(datag.getTipo());
                    return subtotal;
                })
                .switchIfEmpty(Mono.error(new RuntimeException("No existe el producto que busca")))
                .onErrorResume(error -> Mono.just(new Galleta(-1, "No Existe")));
    }
}
