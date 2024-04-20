package me.knighthat.deps.response;

import java.util.List;

public interface ListResponse<T> extends Response {

    List<T> items();
}
