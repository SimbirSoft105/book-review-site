<#import "base.ftlh" as base>
<#import "spring.ftl" as spring/>

<@base.main [] "Добавать новую книгу">
    <section class="add_book">
        <div class="add_book__inner">
            <@spring.bind "addBookForm"/>
            <form class="add_book__body" method="post" enctype="multipart/form-data">
                <div class="profile__photo">
                    <input type="file" name="cover" placeholder="Выбрать фото" class="redact__photo">
                </div>
                <ul class="add_book__info add_book__info-group--change">
                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Название:
                            <@spring.formInput path="addBookForm.title" attributes="class='add_book__info-field' placeholder='Введите название книги'"/>
                            <@spring.showErrors separator="<br>" classOrStyle="error"/>
                        </label>
                    </li>

                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Автор:
                            <@spring.formInput path="addBookForm.author" attributes="class='add_book__info-field' placeholder='Автор книги'"/>
                            <@spring.showErrors separator="<br>" classOrStyle="error"/>
                        </label>
                    </li>

                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Описание:
                            <@spring.formInput path="addBookForm.description" attributes="class='add_book__info-field' placeholder='Описание'"/>
                            <@spring.showErrors separator="<br>" classOrStyle="error"/>
                        </label>
                    </li>

                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Год:
                            <@spring.formInput path="addBookForm.releaseYear" attributes="class='add_book__info-field' placeholder='Год'"/>
                            <@spring.showErrors separator="<br>" classOrStyle="error"/>
                        </label>
                    </li>

                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Язык:
                            <select name="language" class="add_book__info-field">
                                <#list languages as lang>
                                    <option value="${lang.id}">${lang.name}</option>
                                </#list>
                            </select>
                        </label>
                    </li>

                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Страна:
                            <select name="country" class="add_book__info-field">
                                <#list countries as country>
                                    <option value="${country.id}">${country.name}</option>
                                </#list>
                            </select>
                        </label>
                    </li>

                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Категории:
                            <select multiple name="categories" multiselect-search="true">
                                <#list categories as category>
                                    <option value="${category.id}">${category.title}</option>
                                </#list>
                            </select>
                        </label>
                    </li>
                </ul>
                <button type="submit" class="btn__change-data">Сохранить</button>
            </form>
        </div>
    </section>

    <footer class="footer">
        <div class="footer__inner">
            <div class="footer__logo">
                <img src="/images/header/logo-1x.png" alt="" class="footer__logo-img">
            </div>
            <div class="footer__copyright">
                © 2021. All rights reserved.
            </div>
        </div>
    </footer>

    <script src="/js/multiselect-dropdown.js" ></script>

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