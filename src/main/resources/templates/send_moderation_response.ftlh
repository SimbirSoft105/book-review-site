<#import "base.ftlh" as base>

<@base.main [] "Страница модерации">
    <section class="profile">
        <div class="profile__inner">
            <div class="profile__header">
                <h2 class="profile__title">Отклонение размещения</h2>
                <p class="profile__header-desc">Пожалуйста введите причину отказа</p>
            </div>

            <div class="all__films-films">
                <form method="post">
                    <label>
                        <textarea name="response"></textarea>
                    </label>

                    <button type="submit" class="btn__change-data">Сохранить</button>
                </form>
            </div>
        </div>
    </section>

    <footer class="footer">
        <div class="footer__inner">
            <div class="footer__logo">
                <img src="/images/header/logo.png" alt="" class="footer__logo-img">
            </div>
            <div class="footer__copyright">
                © 2021. Все права защищены.
            </div>
        </div>
    </footer>

    <script src="/js/header.js"></script>
</@base.main>