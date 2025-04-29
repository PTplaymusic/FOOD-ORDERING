<%-- 
    Document   : verify-google
    Created on : Apr 27, 2025, 12:39:31 AM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.time.LocalDateTime"%>
<%@page import="java.time.Duration"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Verify Google Account</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
        <style>
            body {
                background: #f5f5f5;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
            }
            .verify-card {
                max-width: 400px;
                width: 100%;
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                padding: 30px;
            }
            .verify-card h2 {
                font-size: 1.5rem;
                margin-bottom: 20px;
                color: #343a40;
            }
            .title-icon {
                margin-right: 8px;
            }
            .error-message {
                color: #dc3545;
                font-size: 0.9rem;
                margin-bottom: 15px;
            }
            .form-control {
                border-radius: 5px;
                margin-bottom: 15px;
            }
            .btn-verify {
                width: 100%;
                background: #28a745;
                border: none;
                border-radius: 5px;
                padding: 10px;
                transition: background 0.3s;
            }
            .btn-verify:hover {
                background: #218838;
            }
            .btn-resend {
                width: 100%;
                background: #007bff;
                border: none;
                border-radius: 5px;
                padding: 10px;
                color: #fff;
                transition: background 0.3s;
                margin-top: 10px;
            }
            .btn-resend:hover {
                background: #0056b3;
            }
            .timer {
                font-size: 1rem;
                color: #007bff;
                margin-bottom: 15px;
                text-align: center;
            }
            .timer.expired {
                color: #dc3545;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <div class="verify-card">
            <h2><i class="bi bi-shield-check title-icon"></i> Enter Verification Code</h2>

            <% if (request.getAttribute("error") != null) {%>
            <p class="error-message"><%= request.getAttribute("error")%></p>
            <% } %>

            <div class="timer" id="countdown">Time remaining: calculating...</div>

            <form action="verify" method="POST" id="verifyForm">
                <input type="text" name="code" class="form-control" placeholder="Enter 6-digit code" required maxlength="6" pattern="\d{6}" title="Please enter a 6-digit code"/>
                <button type="submit" class="btn btn-verify" id="verifyButton"><i class="bi bi-check-circle title-icon"></i> Verify</button>
                <a href="send-verification-email" class="btn btn-resend" id="resendButton"><i class="bi bi-arrow-repeat title-icon"></i> Resend Code</a>
            </form>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Lấy thời gian hết hạn từ server
            <%
                LocalDateTime expiryTime = (LocalDateTime) session.getAttribute("codeExpiryTime");
                long timeLeftSeconds = 0;
                boolean isExpired = false;
                if (expiryTime != null) {
                    Duration duration = Duration.between(LocalDateTime.now(), expiryTime);
                    timeLeftSeconds = Math.max(0, duration.getSeconds());
                    isExpired = timeLeftSeconds <= 0;
                }
            %>

            let timeLeft = <%= timeLeftSeconds%>; // Thời gian còn lại (giây)
            const countdownElement = document.getElementById('countdown');
            const verifyButton = document.getElementById('verifyButton');
            const resendButton = document.getElementById('resendButton');
            const verifyForm = document.getElementById('verifyForm');

            // Kiểm tra nếu mã đã hết hạn ngay khi tải trang
            if (<%= isExpired%> || timeLeft <= 0) {
                countdownElement.textContent = 'Code has expired!';
                countdownElement.classList.add('expired');
                verifyButton.disabled = true;
                verifyButton.innerHTML = '<i class="bi bi-x-circle title-icon"></i> Code Expired';
            } else {
                // Đếm ngược
                const timer = setInterval(() => {
                    if (timeLeft <= 0) {
                        clearInterval(timer);
                        countdownElement.textContent = 'Code has expired!';
                        countdownElement.classList.add('expired');
                        verifyButton.disabled = true;
                        verifyButton.innerHTML = '<i class="bi bi-x-circle title-icon"></i> Code Expired';
                    } else {
                        let minutes = Math.floor(timeLeft / 60);
                        let seconds = timeLeft % 60;
                        seconds = seconds < 10 ? '0' + seconds : seconds;
                        countdownElement.textContent = `Time remaining: ${minutes}:${seconds}`;
                        timeLeft--;
                    }
                }, 1000);
            }

            // Hiệu ứng loading khi submit form
            verifyForm.addEventListener('submit', function () {
                verifyButton.disabled = true;
                verifyButton.innerHTML = '<i class="bi bi-arrow-clockwise title-icon"></i> Verifying...';
            });

            // Hiệu ứng loading khi resend code
            resendButton.addEventListener('click', function (e) {
                resendButton.innerHTML = '<i class="bi bi-arrow-repeat title-icon"></i> Sending...';
                resendButton.classList.add('disabled');
            });
        </script>
    </body>
</html>