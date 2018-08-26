##### The HTTP client

The HTTP client allow you to:
- Execute `GET`, `POST`, `PUT`, `DELETE`, `PATCH` and `HEAD` requests.
- Add query params (i.e URL parameters following `?`).
- Send body (only with `POST`, `PUT` and `PATCH` methods).
- Send headers and cookies.

Here is an example of a `POST` request sending a JSON body:

```java
import com.github.mjeanroy.junit.servers.client.HttpClient;
import com.github.mjeanroy.junit.servers.client.HttpResponse;
import com.github.mjeanroy.junit.servers.jupiter.JunitServerExtension;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.ExtendWith;
import org.junit.jupiter.api.Test;

@ExtendWith(JunitServerExtension.class)
class MyTest {

  @Test
  void should_have_index(HttpClient client) {
    HttpResponse response = client
      .preparePost("/people")
      .setBody("{\"firstName\": \"John\", \"lastName\": \"Doe\"}")
      .asJson()     // Automatically send the `Content-Type` header to `application/json`
      .acceptJson() // Automatically send the `Accept` header to `application/json`
      .addHeader("X-Auth-Token", TOKEN_VALUE)
      .addCookie("X-Auth-Token", TOKEN_VALUE)
      .execute();

    Assertions.assertEquals(204, response.status());
  }
}
```
