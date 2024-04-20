package me.knighthat.deps.response;

import java.util.List;

public interface ListResponse extends Response {

    List<?> execute();
}
