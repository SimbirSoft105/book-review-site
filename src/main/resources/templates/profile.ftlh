<#import "base.ftlh" as base>

<@base.main>
    <section class="profile">
        <div class="profile__inner">
            <div class="profile__header">
                <h2 class="profile__title">Ваш профиль</h2>
                <p class="profile__header-desc">Здесь вы можете редактировать и настраивать ваш профиль</p>
            </div>

            <div class="profile__body">
                <div class="profile__body-inner">
                    <div class="profile__photo">
                        <img <#if user.avatar??>
                                src="${user.avatar}"
                             <#else>
                                src="/images/no_avatar.jpg"
                        </#if> alt="" class="profile__img">
                    </div>
                    <ul class="profile__info">
                        <li class="profile__info-item profile__name"><span>Имя:</span>
                            <span>${user.name}</span></li>
                        <li class="profile__info-item profile__date"><span>Email:</span>
                            <span>${user.email}</span></li>
                    </ul>
                </div>
                <a href="/profile/edit" class="btn__change-data">Изменить данные</a>
            </div>

            <div class="profile__stat">
                <a href="/book/my" class="profile__stat-item">
                    <div class="profile__stat-count">${booksPushedCount}</div>
                    <div class="profile__stat-name">Загружено книг</div>
                </a>
                <a href="/book/favorites" class="profile__stat-item">
                    <div class="profile__stat-count">${favoriteBooksCount}</div>
                    <div class="profile__stat-name">Книг в избранном</div>
                </a>
                <a href="#" class="profile__stat-item">
                    <div class="profile__stat-count">${reviewsWrittenCount}</div>
                    <div class="profile__stat-name">Рецензий</div>
                </a>
            </div>
        </div>
    </section>

    <footer class="footer">
        <div class="footer__inner">
            <div class="footer__logo">
                <img src="/images/header/logo.png" alt="" class="footer__logo-img">
            </div>
            <div class="footer__copyright">
                © 2021. All rights reserved.
            </div>
        </div>
    </footer>

    <script src="/js/header.js"></script>
</@base.main>