<#import "spring.ftl" as spring/>
<#import "base.ftlh" as base>

<@base.main [] "Редактирование профиля">
    <section class="add_book">
        <div class="add_book__inner">
            <div class="profile__header">
                <h2 class="profile__title">Редактирование профиля</h2>
                <p class="profile__header-desc">Здесь вы можете отредактировать ваш профиль</p>
            </div>
            <@spring.bind "editForm"/>
            <form class="add_book__body" method="post" enctype="multipart/form-data">
                <div class="profile__photo">
                    <#if user.avatar??>
                        <img id="image" src="${user.avatar}" alt="" class="profile__img">
                    <#else>
                        <img id="image" src="/images/no_avatar.jpg" alt="" class="profile__img">
                    </#if>
                    <input type="file" name="avatar" onchange="onFileSelected(event)" placeholder="Выбрать фото" class="redact__photo">
                </div>
                <ul class="add_book__info add_book__info-group--change">
                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Полное имя:
                            <@spring.formInput path="editForm.name" attributes="class='add_book__info-field'"/>
                            <@spring.showErrors separator="<br>" classOrStyle="error"/>
                        </label>
                    </li>
                    <li class="add_book__info-group">
                        <label class="add_book__info-label">
                            Email:
                            <@spring.formInput path="editForm.email" attributes="class='add_book__info-field'" fieldType="email"/>
                            <@spring.showErrors separator="<br>" classOrStyle="error"/>
                        </label>
                    </li>
                </ul>
                <button type="submit" class="btn__change-data">Изменить данные</button>
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

    <script>
        function onFileSelected(event) {

            let selectedFile = event.target.files[0];
            let reader = new FileReader();

            let image = document.getElementById("image")

            reader.onload = function(event) {
                image.src = event.target.result;
            };

            reader.readAsDataURL(selectedFile);
        }
    </script>

    <script src="/js/header.js"></script>
</@base.main>