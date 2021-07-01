<!DOCTYPE HTML>
<html lang="en">
<head>
    <title>Письмо</title>
    <meta charset="UTF-8">
    <style>

        *, *:after, *:before {
            box-sizing: border-box;
            margin: 0;
            padding: 0;
        }

        body {
            width: 100%;
            height: 100%;
            display: block;
        }

        .wrapper {
            width: 100%;
            height: 80%;
            min-height: 100vh;
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center
        }

        .letter-content {
            text-align: left;
            width: 50%;
            margin-top: -30px;
            margin-bottom: 30px;
        }

        .picture {
            width: 100%;
            display: flex;
            justify-content: center;
            object-fit: cover;
        }

        .picture-img {
            width: 45%;
            height: auto;
        }

        .confirm-link {
            padding: 10px 35px;
            background-color: #3572b0;
            color: white;
            font-size: 16px;
            font-weight: bold;
            border-radius: 7px;
            text-align: center;
            cursor: pointer;

            text-decoration: none;
        }

        .confirm-link:hover {
            text-decoration: none;
            color: white;
        }

        .btn__email {
            width: 100%;
            margin-top: 25px;
            margin-bottom: 25px;
        }


        .email__text {
            font-size: 18px;
            color: black;
            text-align: left;
            margin-bottom: 15px;
        }

    </style>
</head>
</html>

<body>
<div class="wrapper">
    <div class="letter-content">

        <div class="email__text">
            Здравствуйте, ${name}
        </div>

        <div class="email__text">
            Подтвердите свой адрес электронной почты. Это обязательная процедура,
            позволяющаяя пользоваться нашим сервисом MyBook.
        </div>

        <div class="btn__email">
            <a class="confirm-link" href=${link}>
                Подтвердить адрес электронной почты
            </a>
        </div>

        <div class="email__text">
            С уважением,<br> команда MyBook
        </div>
    </div>
</div>
</body>