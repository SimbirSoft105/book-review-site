<#import "base.ftlh" as base>

<@base.main [] "Контакты">
    <section class="contact--title">
        <div class="contact__bg"></div>
        <div class="contact--title-inner">
            <div class="contact__inner-title">Контакты</div>
            <div class="contact__inner-subtitle">
                <a href="/">Домашняя</a>
                <div class="contact__line">/</div>
                <div>Контакты</div>
            </div>
        </div>
    </section>

    <section class="contacts">
        <div class="contacts__inner">
            <h2 class="contact__title new__movies-title">СВЯЖИТЕСЬ С НАМИ</h2>
            <div class="contacts__left">
<#--                <p class="contacts__text">Ut enim ad minim veniam, quis nostrud exercitation ullamco nisi ut aliquip ex ea-->
<#--                    commodo consequat. Duis aute-->
<#--                    irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.-->
<#--                </p>-->
                <div class="contact__info">
                    <div class="contact__address contact__item">
                        <svg class="location-symbol">
                            <use xlink:href="#location"></use>
                        </svg>
                        <div class="contact__name">Где мы находимся</div>
                        <div class="contact__field">г.Казань, ул.Чистопольская 69</div>
                    </div>
                    <div class="contact__phone contact__item">
                        <svg class="phone-symbol">
                            <use xlink:href="#phone"></use>
                        </svg>
                        <div class="contact__name">Телефон</div>
                        <div class="contact__field">+79274286071</div>
                    </div>
                    <div class="contact__email contact__item">
                        <svg class="email-symbol">
                            <use xlink:href="#email"></use>
                        </svg>
                        <div class="contact__name">EMAIL</div>
                        <div class="contact__field">anvar-geniy@gmail.com</div>
                    </div>
                </div>
            </div>
            <form class="contacts__right" method="post">

            </form>
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