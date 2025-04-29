<%-- 
    Document   : login
    Created on : Apr 25, 2025
    Author     : Grok
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Login - Food Delivery App</title>
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css">
        <style>
            body {
                background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
                min-height: 100vh;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0;
                padding: 0;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .login-card {
                max-width: 900px;
                width: 100%;
                border-radius: 15px;
                box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
                background: #ffffff;
                margin: 20px;
            }
            .nav-tabs {
                border-bottom: none;
                background: #f8f9fa;
                padding: 10px;
                justify-content: center;
                border-radius: 15px 15px 0 0;
            }
            .nav-tabs .nav-link {
                border: none;
                border-radius: 10px;
                margin: 0 5px;
                padding: 10px 20px;
                color: #343a40;
                font-weight: 500;
                transition: all 0.3s ease;
            }
            .nav-tabs .nav-link:hover {
                background: #e9ecef;
                color: #007bff;
            }
            .nav-tabs .nav-link.active {
                background: #007bff;
                color: #fff;
            }
            .tab-pane {
                padding: 30px;
                border-radius: 0 0 15px 15px;
                transition: background-color 0.5s ease, opacity 0.3s ease;
                opacity: 1;
            }
            .tab-pane.fade {
                opacity: 0;
            }
            .tab-pane.fade.show {
                opacity: 1;
            }
            #customer {
                background-color: #d4edda;
            } /* Xanh lá nhạt */
            #shipper {
                background-color: #d1e7dd;
            } /* Xanh dương nhạt */
            #restaurant {
                background-color: #fff3cd;
            } /* Cam nhạt */
            #admin {
                background-color: #e2e3e5;
            } /* Xám nhạt */
            .form-floating label {
                color: #555;
            }
            .btn-social, .btn-login, .btn-register {
                transition: all 0.3s ease;
            }
            .btn-social:hover, .btn-login:hover, .btn-register:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            }
            .btn-login {
                background: #007bff;
                border: none;
                padding: 10px;
            }
            .btn-login:hover {
                background: #0056b3;
            }
            .btn-register {
                border-color: #007bff;
                color: #007bff;
                padding: 8px 16px;
                font-size: 0.9rem;
            }
            .btn-register:hover {
                background: #007bff;
                color: #fff;
            }
            .error-alert {
                animation: fadeIn 0.5s;
                font-size: 0.9rem;
            }
            @keyframes fadeIn {
                from {
                    opacity: 0;
                    transform: translateY(-10px);
                }
                to {
                    opacity: 1;
                    transform: translateY(0);
                }
            }
            .title-icon {
                margin-right: 8px;
            }
            @media (max-width: 576px) {
                .login-card {
                    margin: 10px;
                }
                .nav-tabs .nav-link {
                    padding: 8px 12px;
                    font-size: 14px;
                }
                .tab-pane {
                    padding: 20px;
                }
                .btn-register {
                    padding: 6px 12px;
                    font-size: 0.85rem;
                }
            }
        </style>
    </head>
    <body>
        <div class="login-card">
            <div class="p-4">
                <h2 class="text-center mb-3 fw-bold"><i class="bi bi-box-arrow-in-right title-icon"></i> Welcome to Food Delivery App</h2>
                <p class="text-center text-muted mb-4">
                    Sign in to your account or 
                    <a href="register" class="btn btn-outline-primary btn-register">
                        <i class="bi bi-person-plus title-icon"></i>Create a new one
                    </a>
                </p>

                <!-- Tabs for role selection -->
                <ul class="nav nav-tabs" id="roleTabs" role="tablist">
                    <li class="nav-item" role="presentation">
                        <button class="nav-link active" id="customer-tab" data-bs-toggle="tab" data-bs-target="#customer" type="button" role="tab" aria-controls="customer" aria-selected="true">
                            <i class="bi bi-person title-icon"></i>Customer
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="shipper-tab" data-bs-toggle="tab" data-bs-target="#shipper" type="button" role="tab" aria-controls="shipper" aria-selected="false">
                            <i class="bi bi-truck title-icon"></i>Shipper
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="restaurant-tab" data-bs-toggle="tab" data-bs-target="#restaurant" type="button" role="tab" aria-controls="restaurant" aria-selected="false">
                            <i class="bi bi-shop title-icon"></i>Restaurant
                        </button>
                    </li>
                    <li class="nav-item" role="presentation">
                        <button class="nav-link" id="admin-tab" data-bs-toggle="tab" data-bs-target="#admin" type="button" role="tab" aria-controls="admin" aria-selected="false">
                            <i class="bi bi-shield-lock title-icon"></i>Admin
                        </button>
                    </li>
                </ul>

                <!-- Tab content -->
                <div class="tab-content">
                    <!-- Customer Login -->
                    <div class="tab-pane fade show active" id="customer" role="tabpanel" aria-labelledby="customer-tab">
                        <div class="row gy-4 justify-content-center">
                            <div class="col-12 col-lg-5">
                                <form action="login" method="POST" class="login-form">
                                    <input type="hidden" name="role" value="customer">
                                    <div class="row gy-3">
                                        <div class="col-12">
                                            <div class="form-floating">
                                                <input type="email" class="form-control rounded-4" name="email" id="customer_email" placeholder="name@example.com" required>
                                                <label for="customer_email">Email</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="form-floating">
                                                <input type="password" class="form-control rounded-4" name="password" id="customer_password" placeholder="Password" required>
                                                <label for="customer_password">Password</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="row justify-content-between align-items-center">
                                                <div class="col-6">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" name="remember_me" id="customer_remember_me">
                                                        <label class="form-check-label text-muted" for="customer_remember_me">Remember me</label>
                                                    </div>
                                                </div>
                                                <div class="col-6 text-end">
                                                    <a href="#!" class="text-decoration-none text-primary">Forgot password?</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <button class="btn btn-login w-100 rounded-4 py-2" type="submit">
                                                <i class="bi bi-box-arrow-in-right title-icon"></i>Log in
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="col-12 col-lg-2 d-flex align-items-center justify-content-center">
                                <div class="text-muted">or</div>
                            </div>
                            <div class="col-12 col-lg-5">
                                <div class="d-flex flex-column gap-3">
                                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/SWPProjectV1/login-google&response_type=code&client_id=867859741686-1u47odeeuukpntrhnroar7694gdajp4t.apps.googleusercontent.com&approval_prompt=force&state=customer"
                                       class="btn btn-outline-danger btn-social w-100 rounded-4 py-2">
                                        <i class="bi bi-google title-icon"></i>Continue with Google
                                    </a>
                                    <a href="https://www.facebook.com/v10.0/dialog/oauth?client_id=YOUR_FACEBOOK_APP_ID&redirect_uri=http://localhost:8080/SWPProject/login&scope=email,public_profile&state=customer"
                                       class="btn btn-outline-primary btn-social w-100 rounded-4 py-2">
                                        <i class="bi bi-facebook title-icon"></i>Continue with Facebook
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Shipper Login -->
                    <div class="tab-pane fade" id="shipper" role="tabpanel" aria-labelledby="shipper-tab">
                        <div class="row gy-4 justify-content-center">
                            <div class="col-12 col-lg-5">
                                <form action="login" method="POST" class="login-form">
                                    <input type="hidden" name="role" value="shipper">
                                    <div class="row gy-3">
                                        <div class="col-12">
                                            <div class="form-floating">
                                                <input type="email" class="form-control rounded-4" name="email" id="shipper_email" placeholder="name@example.com" required>
                                                <label for="shipper_email">Email</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="form-floating">
                                                <input type="password" class="form-control rounded-4" name="password" id="shipper_password" placeholder="Password" required>
                                                <label for="shipper_password">Password</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="row justify-content-between align-items-center">
                                                <div class="col-6">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" name="remember_me" id="shipper_remember_me">
                                                        <label class="form-check-label text-muted" for="shipper_remember_me">Remember me</label>
                                                    </div>
                                                </div>
                                                <div class="col-6 text-end">
                                                    <a href="#!" class="text-decoration-none text-primary">Forgot password?</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <button class="btn btn-login w-100 rounded-4 py-2" type="submit">
                                                <i class="bi bi-box-arrow-in-right title-icon"></i>Log in
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="col-12 col-lg-2 d-flex align-items-center justify-content-center">
                                <div class="text-muted">or</div>
                            </div>
                            <div class="col-12 col-lg-5">
                                <div class="d-flex flex-column gap-3">
                                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/SWPProjectV1/login-google&response_type=code&client_id=867859741686-1u47odeeuukpntrhnroar7694gdajp4t.apps.googleusercontent.com&approval_prompt=force&state=shipper"
                                       class="btn btn-outline-danger btn-social w-100 rounded-4 py-2">
                                        <i class="bi bi-google title-icon"></i>Continue with Google
                                    </a>
                                    <a href="https://www.facebook.com/v10.0/dialog/oauth?client_id=YOUR_FACEBOOK_APP_ID&redirect_uri=http://localhost:8080/SWPProject/login&scope=email,public_profile&state=shipper"
                                       class="btn btn-outline-primary btn-social w-100 rounded-4 py-2">
                                        <i class="bi bi-facebook title-icon"></i>Continue with Facebook
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Restaurant Login -->
                    <div class="tab-pane fade" id="restaurant" role="tabpanel" aria-labelledby="restaurant-tab">
                        <div class="row gy-4 justify-content-center">
                            <div class="col-12 col-lg-5">
                                <form action="login" method="POST" class="login-form">
                                    <input type="hidden" name="role" value="restaurant">
                                    <div class="row gy-3">
                                        <div class="col-12">
                                            <div class="form-floating">
                                                <input type="email" class="form-control rounded-4" name="email" id="restaurant_email" placeholder="name@example.com" required>
                                                <label for="restaurant_email">Email</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="form-floating">
                                                <input type="password" class="form-control rounded-4" name="password" id="restaurant_password" placeholder="Password" required>
                                                <label for="restaurant_password">Password</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="row justify-content-between align-items-center">
                                                <div class="col-6">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" name="remember_me" id="restaurant_remember_me">
                                                        <label class="form-check-label text-muted" for="restaurant_remember_me">Remember me</label>
                                                    </div>
                                                </div>
                                                <div class="col-6 text-end">
                                                    <a href="#!" class="text-decoration-none text-primary">Forgot password?</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <button class="btn btn-login w-100 rounded-4 py-2" type="submit">
                                                <i class="bi bi-box-arrow-in-right title-icon"></i>Log in
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="col-12 col-lg-2 d-flex align-items-center justify-content-center">
                                <div class="text-muted">or</div>
                            </div>
                            <div class="col-12 col-lg-5">
                                <div class="d-flex flex-column gap-3">
                                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/SWPProjectV1/login-google&response_type=code&client_id=867859741686-1u47odeeuukpntrhnroar7694gdajp4t.apps.googleusercontent.com&approval_prompt=force&state=restaurant"
                                       class="btn btn-outline-danger btn-social w-100 rounded-4 py-2">
                                        <i class="bi bi-google title-icon"></i>Continue with Google
                                    </a>
                                    <a href="https://www.facebook.com/v10.0/dialog/oauth?client_id=YOUR_FACEBOOK_APP_ID&redirect_uri=http://localhost:8080/SWPProject/login&scope=email,public_profile&state=restaurant"
                                       class="btn btn-outline-primary btn-social w-100 rounded-4 py-2">
                                        <i class="bi bi-facebook title-icon"></i>Continue with Facebook
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Admin Login -->
                    <div class="tab-pane fade" id="admin" role="tabpanel" aria-labelledby="admin-tab">
                        <div class="row gy-4 justify-content-center">
                            <div class="col-12 col-lg-5">
                                <form action="login" method="POST" class="login-form">
                                    <input type="hidden" name="role" value="admin">
                                    <div class="row gy-3">
                                        <div class="col-12">
                                            <div class="form-floating">
                                                <input type="email" class="form-control rounded-4" name="email" id="admin_email" placeholder="name@example.com" required>
                                                <label for="admin_email">Email</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="form-floating">
                                                <input type="password" class="form-control rounded-4" name="password" id="admin_password" placeholder="Password" required>
                                                <label for="admin_password">Password</label>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <div class="row justify-content-between align-items-center">
                                                <div class="col-6">
                                                    <div class="form-check">
                                                        <input class="form-check-input" type="checkbox" name="remember_me" id="admin_remember_me">
                                                        <label class="form-check-label text-muted" for="admin_remember_me">Remember me</label>
                                                    </div>
                                                </div>
                                                <div class="col-6 text-end">
                                                    <a href="#!" class="text-decoration-none text-primary">Forgot password?</a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <button class="btn btn-login w-100 rounded-4 py-2" type="submit">
                                                <i class="bi bi-box-arrow-in-right title-icon"></i>Log in
                                            </button>
                                        </div>
                                    </div>
                                </form>
                            </div>
                            <div class="col-12 col-lg-2 d-flex align-items-center justify-content-center">
                                <div class="text-muted">or</div>
                            </div>
                            <div class="col-12 col-lg-5">
                                <div class="d-flex flex-column gap-3">
                                    <a href="https://accounts.google.com/o/oauth2/auth?scope=email%20profile%20openid&redirect_uri=http://localhost:8080/SWPProjectV1/login-google&response_type=code&client_id=867859741686-1u47odeeuukpntrhnroar7694gdajp4t.apps.googleusercontent.com&approval_prompt=force&state=admin"
                                       class="btn btn-outline-danger btn-social w-100 rounded-4 py-2">
                                        <i class="bi bi-google title-icon"></i>Continue with Google
                                    </a>
                                    <a href="https://www.facebook.com/v10.0/dialog/oauth?client_id=YOUR_FACEBOOK_APP_ID&redirect_uri=http://localhost:8080/SWPProject/login&scope=email,public_profile&state=admin"
                                       class="btn btn-outline-primary btn-social w-100 rounded-4 py-2">
                                        <i class="bi bi-facebook title-icon"></i>Continue with Facebook
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!-- Error message -->
                <% if (request.getAttribute("error") != null) {%>
                <div class="alert alert-danger error-alert mt-4 mx-4 rounded-4" role="alert">
                    <i class="bi bi-exclamation-circle title-icon"></i><%= request.getAttribute("error")%>
                </div>
                <% }%>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
        <script>
            // Tự động focus vào trường email khi chuyển tab
            document.querySelectorAll('.nav-link').forEach(tab => {
                tab.addEventListener('shown.bs.tab', function (event) {
                    const targetTab = event.target.getAttribute('data-bs-target').substring(1); // Lấy ID của tab (customer, shipper, v.v.)
                    const emailInput = document.getElementById(`${targetTab}_email`);
                    if (emailInput) {
                        emailInput.focus();
                    }
                });
            });

            // Hiệu ứng loading khi submit form
            document.querySelectorAll('.login-form').forEach(form => {
                form.addEventListener('submit', function (e) {
                    const submitButton = form.querySelector('.btn-login');
                    submitButton.disabled = true;
                    submitButton.innerHTML = '<i class="bi bi-arrow-clockwise title-icon"></i>Logging in...';
                });
            });

            // Hiệu ứng loading cho nút social login
            document.querySelectorAll('.btn-social').forEach(button => {
                button.addEventListener('click', function () {
                    this.innerHTML = '<i class="bi bi-arrow-clockwise title-icon"></i>Connecting...';
                    this.classList.add('disabled');
                });
            });

            // Hiệu ứng loading cho nút register
            document.querySelector('.btn-register').addEventListener('click', function () {
                this.innerHTML = '<i class="bi bi-arrow-clockwise title-icon"></i>Redirecting...';
                this.classList.add('disabled');
            });
        </script>
    </body>
</html>