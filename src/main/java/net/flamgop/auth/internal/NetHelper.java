package net.flamgop.auth.internal;

import com.squareup.moshi.JsonAdapter;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Function;

public final class NetHelper {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private NetHelper() {}

    public static <T, R> CompletableFuture<R> post(
            OkHttpClient client,
            String url,
            T payload,
            JsonAdapter<T> requestAdapter,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator
    ) {
        return post(client, url, payload, requestAdapter, responseAdapter, validator, builder -> {});
    }

    public static <T, R> CompletableFuture<R> post(
            OkHttpClient client,
            String url,
            T payload,
            JsonAdapter<T> requestAdapter,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator,
            Consumer<Request.Builder> headerCustomizer
    ) {
        RequestBody body = RequestBody.create(requestAdapter.toJson(payload), JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json");

        headerCustomizer.accept(requestBuilder);
        return enqueue(client, requestBuilder.build(), responseAdapter, validator);
    }

    public static <R> CompletableFuture<R> postBody(
            OkHttpClient client,
            String url,
            RequestBody body,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator,
            Consumer<Request.Builder> headerCustomizer
    ) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .post(body)
                .addHeader("Accept", "application/json");

        headerCustomizer.accept(requestBuilder);
        return enqueue(client, requestBuilder.build(), responseAdapter, validator);
    }

    public static <T, R> CompletableFuture<R> put(
            OkHttpClient client,
            String url,
            T payload,
            JsonAdapter<T> requestAdapter,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator,
            Consumer<Request.Builder> headerCustomizer
    ) {
        RequestBody body = RequestBody.create(requestAdapter.toJson(payload), JSON);
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json");

        headerCustomizer.accept(requestBuilder);
        return enqueue(client, requestBuilder.build(), responseAdapter, validator);
    }

    public static <R> CompletableFuture<R> putBody(
            OkHttpClient client,
            String url,
            RequestBody body,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator,
            Consumer<Request.Builder> headerCustomizer
    ) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("Accept", "application/json");

        headerCustomizer.accept(requestBuilder);
        return enqueue(client, requestBuilder.build(), responseAdapter, validator);
    }

    public static <R> CompletableFuture<R> get(
            OkHttpClient client,
            String url,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator
    ) {
        return get(client, url, responseAdapter, validator, builder -> {});
    }

    public static <R> CompletableFuture<R> get(
            OkHttpClient client,
            String url,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator,
            Consumer<Request.Builder> headerCustomizer
    ) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Accept", "application/json");

        headerCustomizer.accept(requestBuilder);
        return enqueue(client, requestBuilder.build(), responseAdapter, validator);
    }

    public static <R> CompletableFuture<R> delete(
            OkHttpClient client,
            String url,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator
    ) {
        return delete(client, url, responseAdapter, validator, builder -> {});
    }

    public static <R> CompletableFuture<R> delete(
            OkHttpClient client,
            String url,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator,
            Consumer<Request.Builder> headerCustomizer
    ) {
        Request.Builder requestBuilder = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("Accept", "application/json");

        headerCustomizer.accept(requestBuilder);
        return enqueue(client, requestBuilder.build(), responseAdapter, validator);
    }

    private static <R> CompletableFuture<R> enqueue(
            OkHttpClient client,
            Request request,
            JsonAdapter<R> responseAdapter,
            Function<R, String> validator
    ) {
        CompletableFuture<R> future = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                future.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) {
                try (response) {
                    String responseBodyString = response.body().string();
                    if (!response.isSuccessful()) {
                        future.completeExceptionally(
                                new RuntimeException("Request failed with HTTP " + response.code() + ": " + responseBodyString)
                        );
                        return;
                    }

                    R dto = responseAdapter.fromJson(responseBodyString);
                    if (dto == null) {
                        future.completeExceptionally(new RuntimeException("Malformed response payload: null DTO"));
                        return;
                    }

                    String validationError = validator != null ? validator.apply(dto) : null;
                    if (validationError != null) {
                        future.completeExceptionally(new RuntimeException(validationError));
                        return;
                    }

                    future.complete(dto);
                } catch (Exception e) {
                    future.completeExceptionally(e);
                }
            }
        });

        return future;
    }
}
