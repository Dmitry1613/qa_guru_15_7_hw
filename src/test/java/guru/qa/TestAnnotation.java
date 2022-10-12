package guru.qa;

import com.codeborne.selenide.*;
import guru.qa.data.SolutionName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import java.util.List;
import java.util.stream.Stream;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class TestAnnotation {

    @DisplayName("Тест с помощью аннотации @ValueSource")
    @ValueSource(strings = {"3050", "3080"})
    @ParameterizedTest(name = "Проверка числа результатов поиска видеокарты на сайте Nvidia для видеокарты - {0}")
    void nvidiaSearchProducts(String testData) {
        open("https://www.nvidia.com/ru-ru/");
        $("[aria-label = 'Search NVIDIA']").click();
        $("#search-terms").setValue(testData).pressEnter();
        $$(".search-results-item")
          .shouldHave(CollectionCondition.size(10))
          .first()
          .shouldHave(text(testData));
        ;
    }

    @CsvSource({
      "3050, Игровые видеокарты GeForce RTX 3050 | NVIDIA",
      "3080, Игровые видеокарты GeForce RTX 3080 и RTX 3080 Ti"
    })
    @DisplayName("Тест с помощью аннотации @CsvSource")
    @ParameterizedTest(name = "Проверка числа результатов поиска на сайте Nvidia для видеокарты - {0}")
    void nvidiaSearchCommonTestDifferentExpectedText(String searchQuery, String expectedText) {
        open("https://www.nvidia.com/ru-ru/");
        $("[aria-label = 'Search NVIDIA']").click();
        $("#search-terms").setValue(searchQuery).pressEnter();
        $$(".search-results-item")
          .shouldHave(CollectionCondition.size(10))
          .first()
          .shouldHave(text(expectedText));
    }

    static Stream<Arguments> nvidiaSiteMenuTextDataProvider() {
        return Stream.of(
          Arguments.of(List.of("Преимущества", "Возможности", "Пользователи", "Отрасли", "Программное обеспечение", "Попробовать"), SolutionName.Виртуализация),
          Arguments.of(List.of("Жизненный цикл MLOps", "Партнеры"), SolutionName.MLOps)
        );
    }
    @DisplayName("Тест с помощью аннотации @MethodSource")
    @MethodSource("nvidiaSiteMenuTextDataProvider")
    @ParameterizedTest(name = "Проверка кнопок на странице для решения - {1}")
    void nvidiaSiteButtonsText(List <String> buttonsText, SolutionName solutionName) {
        open("https://www.nvidia.com/ru-ru/");
        Configuration.holdBrowserOpen = true;
        $$(".nv-menu-button.menu-level-1").find(text("Решения")).click();
        $$("li.menu-level-4").find(text(solutionName.name())).click();
        $$(".breadcrumb-list li").filter(visible).shouldHave(CollectionCondition.texts(buttonsText));
    }
}