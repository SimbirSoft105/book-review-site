<#import "spring.ftl" as spring/>
<#import "base.ftlh" as base>

<@base.main [] "Редактирование профиля">
    <section class="profile">
        <div class="profile__inner">
            <@spring.bind "editForm"/>
            <form class="profile__body" method="post" enctype="multipart/form-data">
                <div class="profile__photo">
                    <img id="image" src="/images/profile/user-no-big.gif" alt="" class="profile__img">
                    <input type="file" name="avatar" onchange="onFileSelected(event)" placeholder="Выбрать фото" class="redact__photo">
                </div>
                <ul class="profile__info profile__info-group--change">
                    <li class="profile__info-group">
                        <label class="profile__info-label">
                            Полное имя:
<#--                            <input type="text" name="name" class="profile__info-field">-->
                            <@spring.formInput path="editForm.name" attributes="class='profile__info-field'"/>
                            <@spring.showErrors separator="<br>" classOrStyle="error"/>
                        </label>
                    </li>
                    <li class="profile__info-group">
                        <label class="profile__info-label">
                            Email:
<#--                            <input type="text" name="email" class="profile__info-field">-->
                            <@spring.formInput path="editForm.email" attributes="class='profile__info-field'" fieldType="email"/>
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