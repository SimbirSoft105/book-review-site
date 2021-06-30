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
                <img src="../static/images/header/logo-1x.png" alt="" class="footer__logo-img">
            </div>
            <div class="footer__copyright">
                © 2021. All rights reserved.
            </div>
        </div>
    </footer>

    <script>
        const mainPage = document.querySelector('#page');
        const menu = document.querySelector(".burger");
        const closeBtn = document.querySelector(".close__block");
        const links = document.querySelector("#links");
        const allLinks = document.querySelectorAll(".nav__link-item");
        const header = document.querySelector('#header');
        const btnProfile = document.querySelector('.header__profile--svg');
        const profileContent = document.querySelector('.header__profile-content');

        const allFilterMovies = document.querySelectorAll(".movies__filter-item");
        const openMenu = () => {
            if (links.className.includes("open")) {
                links.classList.remove('open');
                allLinks.forEach((item) => {
                    item.classList.remove("fade");
                });
                mainPage.classList.remove("overflow-hidden");
            } else {
                links.classList.add('open');
                allLinks.forEach((item) => {
                    item.classList.add("fade");
                });
                mainPage.classList.add("overflow-hidden");
            }
        };

        const closeMenu = () => {
            if (links.className.includes("open")) {
                links.classList.remove('open');
                allLinks.forEach((item) => {
                    item.classList.remove("fade");
                });
                mainPage.classList.remove("overflow-hidden");
            }
        };

        const tooltips = () => {
            profileContent.classList.toggle('vis');
        };


        menu.addEventListener("click", openMenu);
        closeBtn.addEventListener("click", closeMenu);
        allLinks.forEach((item) => {
            item.addEventListener("click", closeMenu);
        });
        allFilterMovies.forEach((item) => {
            item.addEventListener("click", function (e) {
                e.preventDefault();
                if (!e.target.getAttribute('class').includes('filter-active')) {
                    allFilterMovies.forEach((i) => {
                        i.classList.remove('filter-active');
                    });
                    this.classList.add('filter-active');
                }
            });
        });
        btnProfile.addEventListener("click", tooltips);
    </script>
</@base.main>