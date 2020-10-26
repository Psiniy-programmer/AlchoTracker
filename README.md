# <center>**Техническое задание**</center>
## Название
**Alchotracker**
## Назначение
Мобильное приложение для контроля употребления алкоголя
## Целевая аудитория
Студенты, ~~школьники~~, люди у которых проблемы с контролем употребления алкоголя.

# <center>[ Прототип ](https://marvelapp.com/prototype/f8j9fd7/screen/73906707)</center>


# <center>Функциональность приложения</center>
1. Календарь с отметками
    * Дни принятия алкоголя в себя
    * Расходы на мероприятия
    * Что и сколько выпил
2. Аунтификация
    * Гугл почта
3. Профиль пользователя
    * Аватарка
    * Статус(описание)
    * Предпочтения в алкоголе
    * Экспорт профиля
4. Таймер
    * Время старта и окончания мероприятия
    * Напоминания о перерывах
5. Push уведомления
    * Отправка пользователю уведомлений
6. Статистика
    * Выдача статистики пользователю (графики, проценты и тд)
    * Сравнения статистики по месяцам
7. Организация
    * Список друзей
    * Создание событий, организатор по списку друзей приглашает людей в чат, где они смогут обозначить что будут пить и сколько это выйдет по деньгам. После чего будет выдан список и кол-во необходимых денег с каждого
8. Для работы с сетью напишем свой сервер

# <center> Описание экранов приложения </center>
1. Экран **Авторизации** <br> На этом экране пользователь будет авторизовываться через гугл почту 
![Auth view](img/Auth_view.png)
2. Экран **Каленадря** <br> На данном эркане пользователь сможет:<br>
   - планировать события на определенные дни.
   - получать "быстрые сведения" (кол-во планируемых потраченных денег и тд.) по кликнутому дню.
   - Создание мероприятия с приглашением пользователей из списка друзей
  
![Kal view](img/Kal_view.png) <br>
3. Экран **Профиля** <br> На этом экране будет находиться профиль пользователя и следующие данные о нем :<br>
   - Статус(описание пользователя)
   - Аватарка
   - Кол-во друзей
   - Кол-во посещенных им мероприятий

Функции доступные в профиле :<br>
   - Экспорт профиля
   - Получение списка друзей пользователя

![Profile view](img/Profile_view.png) <br>
4. Экран **Списка друзей** <br> На этом экране будет рапсологаться список друзей, функционал данного экрана:<br>
   - По клику на аватарку откроется профиль друга
   - По клику итема друга откроется чат с этим другом
   - Добавление друзей
   - DropDown кнопка, в которой будет переключение на список мероприятий

![Friends_view](img/Friends_view.png) <br>
5. Экран **Мероприятий** <br> На этом экране будет располагаться список меропритяий, функционал данного экрана:<br>
   - Создать мероприятие
   - Переход в чат по клику на итем мероприятия

![Soical_view](img/Social_view.png) <br>
6. Экран **Чата** <br> На этом экране будет располагаться самый обычный чат для отправки **текстовых** сообщений

![Chat_view](img/Chat_view.png)

7. Экран **Таймера** <br> На этом экране будет располагаться таймер, функционал данного экрана: <br>
    - Старт отчета времени
    - Окончание отчета времени
    - Напоминания о перерывах
    - Напоминания о желательном кол-ве выпитого

![Timer_view](img/Timer_view.png) <br>
8. Экран **Статистики** <br> На этом экране будет расополгаться статистика пользователя <br>
![Stat_view](img/Stat_view.png) <br>

# Схема переходов между экранами
![Scheme](img/Screens.png)