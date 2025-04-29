<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Customers" %>
<%@ page import="model.Shippers" %>
<%@ page import="model.Restaurants" %>

<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            body {
                background: #f5f5f5;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .sidebar {
                height: 100vh;
                background: linear-gradient(180deg, #343a40, #495057);
                padding-top: 20px;
                position: fixed;
                width: 220px;
                color: #fff;
            }
            .sidebar .nav-link {
                color: #adb5bd;
                transition: color 0.3s;
            }
            .sidebar .nav-link:hover {
                color: #fff;
                background: #495057;
                border-radius: 5px;
            }
            .sidebar h4 {
                color: #fff;
                text-align: center;
                margin-bottom: 20px;
            }
            .main-content {
                margin-left: 220px;
                padding: 30px;
            }
            .chart-card {
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                padding: 15px;
                margin-bottom: 20px;
            }
            .table-container {
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                padding: 15px;
                margin-bottom: 20px;
            }
            .table-wrapper {
                max-height: 300px;
                overflow-y: auto;
                border: 1px solid #dee2e6;
                border-radius: 5px;
            }
            .status-active {
                color: #28a745;
                font-weight: bold;
            }
            .status-pending {
                color: #ffc107;
                font-weight: bold;
            }
            .status-banned {
                color: #dc3545;
                font-weight: bold;
            }
            .status-bar {
                margin-bottom: 10px;
            }
            .status-bar span {
                margin-right: 15px;
                font-size: 14px;
                padding: 5px 10px;
                border-radius: 15px;
                background: #f1f3f5;
            }
            .table thead {
                background: #e9ecef;
                position: sticky;
                top: 0;
                z-index: 1;
            }
            .table th, .table td {
                vertical-align: middle;
            }
            .btn-action {
                margin-right: 5px;
            }
            .search-bar {
                margin-bottom: 15px;
            }
            .title-icon {
                margin-right: 8px;
            }
        </style>
    </head>

    <body id="main-body">
        <div class="container-fluid">
            <div class="row">
                <!-- Sidebar -->
                <nav class="col-md-2 sidebar p-3">
                    <h4>Admin Panel</h4>
                    <ul class="nav flex-column">
                        <li class="nav-item"><a class="nav-link active" href="admin"><i class="bi bi-speedometer2 title-icon"></i> Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="bi bi-person title-icon"></i> Manage Customers</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="bi bi-truck title-icon"></i> Manage Shippers</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="bi bi-shop title-icon"></i> Manage Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link" href="manage-payments"><i class="bi bi-wallet2 title-icon"></i> Manage Payments</a></li>
                        <li class="nav-item"><a class="nav-link" href="pending-accounts"><i class="bi bi-check-circle title-icon"></i> Pending Approvals</a></li>
                        <li class="nav-item"><a class="nav-link" href="logout"><i class="bi bi-box-arrow-right title-icon"></i> Logout</a></li>
                    </ul>
                    <hr style="border-color: #adb5bd;">
                    <div>
                        <label class="text-light">Theme:</label>
                        <select onchange="changeTheme(this.value)" class="form-select mt-2">
                            <option value="#f5f5f5">Light</option>
                            <option value="#d0e7ff">Blue</option>
                            <option value="#ffd1dc">Pink</option>
                        </select>
                    </div>
                </nav>

                <!-- Main Content -->
                <main class="col-md-10 main-content">
                    <h2 class="mt-4 mb-4"><i class="bi bi-speedometer2 title-icon"></i> Dashboard</h2>
                    <div class="row">
                        <div class="col-md-4 chart-card">
                            <h5 class="text-center">Số đơn hàng (Customer)</h5>
                            <canvas id="customerChart"></canvas>
                        </div>
                        <div class="col-md-4 chart-card">
                            <h5 class="text-center">Doanh thu (Restaurant)</h5>
                            <canvas id="restaurantChart"></canvas>
                        </div>
                        <div class="col-md-4 chart-card">
                            <h5 class="text-center">Số đơn giao (Shipper)</h5>
                            <canvas id="shipperChart"></canvas>
                        </div>
                    </div>

                    <%
                        List<Customers> customers = (List<Customers>) request.getAttribute("customers");
                        List<Shippers> shippers = (List<Shippers>) request.getAttribute("shippers");
                        List<Restaurants> restaurants = (List<Restaurants>) request.getAttribute("restaurants");

                        // Tính số liệu trạng thái
                        int customerActive = (int) customers.stream().filter(c -> c.getStatusId() == 1).count();
                        int customerPending = (int) customers.stream().filter(c -> c.getStatusId() == 0).count();
                        int customerBanned = (int) customers.stream().filter(c -> c.getStatusId() == 3).count();

                        int shipperActive = (int) shippers.stream().filter(s -> s.getStatusId() == 1).count();
                        int shipperPending = (int) shippers.stream().filter(s -> s.getStatusId() == 0).count();
                        int shipperBanned = (int) shippers.stream().filter(s -> s.getStatusId() == 3).count();

                        int restaurantActive = (int) restaurants.stream().filter(r -> r.getStatusId() == 1).count();
                        int restaurantPending = (int) restaurants.stream().filter(r -> r.getStatusId() == 0).count();
                        int restaurantBanned = (int) restaurants.stream().filter(r -> r.getStatusId() == 3).count();
                    %>

                    <!-- Customer Table -->
                    <div class="table-container">
                        <h4><i class="bi bi-person title-icon"></i> Customer Accounts</h4>
                        <div class="status-bar">
                            <span class="status-active">Active: <%= customerActive%></span>
                            <span class="status-pending">Pending: <%= customerPending%></span>
                            <span class="status-banned">Banned: <%= customerBanned%></span>
                        </div>
                        <div class="search-bar">
                            <input type="text" id="customerSearch" class="form-control" placeholder="Tìm kiếm theo tên, email, số điện thoại..." onkeyup="searchTable('customerSearch', 'customerTable')">
                        </div>
                        <div class="table-wrapper">
                            <table class="table table-bordered" id="customerTable">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Phone</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% int customerIndex = 1; %>
                                    <% for (Customers c : customers) {%>
                                    <tr>
                                        <td><%= customerIndex++%></td>
                                        <td><%= c.getName()%></td>
                                        <td><%= c.getEmail()%></td>
                                        <td><%= c.getPhone()%></td>
                                        <td class="status-<%= getStatusText(c.getStatusId()).toLowerCase()%>"><%= getStatusText(c.getStatusId())%></td>
                                        <td>
                                            <a href="edit-customer?id=<%= c.getCustomerId()%>" class="btn btn-sm btn-primary btn-action">Edit</a>
                                        </td>
                                    </tr>
                                    <% } %>
                                    <!-- Thêm dữ liệu giả lập để kiểm tra chiều cao -->
                                    <% for (int i = customerIndex; i <= 10; i++) {%>
                                    <tr>
                                        <td><%= i%></td>
                                        <td>Khách hàng <%= i%></td>
                                        <td>khachhang<%= i%>@gmail.com</td>
                                        <td>09012345<%= i%></td>
                                        <td class="status-active">Active</td>
                                        <td>
                                            <a href="#" class="btn btn-sm btn-primary btn-action">Edit</a>
                                        </td>
                                    </tr>
                                    <% }%>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Shipper Table -->
                    <div class="table-container">
                        <h4><i class="bi bi-truck title-icon"></i> Shipper Accounts</h4>
                        <div class="status-bar">
                            <span class="status-active">Active: <%= shipperActive%></span>
                            <span class="status-pending">Pending: <%= shipperPending%></span>
                            <span class="status-banned">Banned: <%= shipperBanned%></span>
                        </div>
                        <div class="search-bar">
                            <input type="text" id="shipperSearch" class="form-control" placeholder="Tìm kiếm theo tên, email, số điện thoại..." onkeyup="searchTable('shipperSearch', 'shipperTable')">
                        </div>
                        <div class="table-wrapper">
                            <table class="table table-bordered" id="shipperTable">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Phone</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% int shipperIndex = 1; %>
                                    <% for (Shippers s : shippers) {%>
                                    <tr>
                                        <td><%= shipperIndex++%></td>
                                        <td><%= s.getName()%></td>
                                        <td><%= s.getEmail()%></td>
                                        <td><%= s.getPhone()%></td>
                                        <td class="status-<%= getStatusText(s.getStatusId()).toLowerCase()%>"><%= getStatusText(s.getStatusId())%></td>
                                        <td>
                                            <a href="edit-shipper?id=<%= s.getShipperId()%>" class="btn btn-sm btn-primary btn-action">Edit</a>
                                        </td>
                                    </tr>
                                    <% } %>
                                    <!-- Thêm dữ liệu giả lập để kiểm tra chiều cao -->
                                    <% for (int i = shipperIndex; i <= 10; i++) {%>
                                    <tr>
                                        <td><%= i%></td>
                                        <td>Shipper <%= i%></td>
                                        <td>shipper<%= i%>@gmail.com</td>
                                        <td>09012345<%= i%></td>
                                        <td class="status-active">Active</td>
                                        <td>
                                            <a href="#" class="btn btn-sm btn-primary btn-action">Edit</a>
                                        </td>
                                    </tr>
                                    <% }%>
                                </tbody>
                            </table>
                        </div>
                    </div>

                    <!-- Restaurant Table -->
                    <div class="table-container">
                        <h4><i class="bi bi-shop title-icon"></i> Restaurant Accounts</h4>
                        <div class="status-bar">
                            <span class="status-active">Active: <%= restaurantActive%></span>
                            <span class="status-pending">Pending: <%= restaurantPending%></span>
                            <span class="status-banned">Banned: <%= restaurantBanned%></span>
                        </div>
                        <div class="search-bar">
                            <input type="text" id="restaurantSearch" class="form-control" placeholder="Tìm kiếm theo tên, email, số điện thoại..." onkeyup="searchTable('restaurantSearch', 'restaurantTable')">
                        </div>
                        <div class="table-wrapper">
                            <table class="table table-bordered" id="restaurantTable">
                                <thead>
                                    <tr>
                                        <th>STT</th>
                                        <th>Name</th>
                                        <th>Email</th>
                                        <th>Phone</th>
                                        <th>Status</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <% int restaurantIndex = 1; %>
                                    <% for (Restaurants r : restaurants) {%>
                                    <tr>
                                        <td><%= restaurantIndex++%></td>
                                        <td><%= r.getName()%></td>
                                        <td><%= r.getEmail()%></td>
                                        <td><%= r.getPhone()%></td>
                                        <td class="status-<%= getStatusText(r.getStatusId()).toLowerCase()%>"><%= getStatusText(r.getStatusId())%></td>
                                        <td>
                                            <a href="edit-restaurant?id=<%= r.getRestaurantId()%>" class="btn btn-sm btn-primary btn-action">Edit</a>
                                        </td>
                                    </tr>
                                    <% } %>
                                    <!-- Thêm dữ liệu giả lập để kiểm tra chiều cao -->
                                    <% for (int i = restaurantIndex; i <= 10; i++) {%>
                                    <tr>
                                        <td><%= i%></td>
                                        <td>Nhà hàng <%= i%></td>
                                        <td>nhahang<%= i%>@gmail.com</td>
                                        <td>09012345<%= i%></td>
                                        <td class="status-active">Active</td>
                                        <td>
                                            <a href="#" class="btn btn-sm btn-primary btn-action">Edit</a>
                                        </td>
                                    </tr>
                                    <% }%>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </main>
            </div>
        </div>

        <script>
            function changeTheme(color) {
                document.getElementById('main-body').style.background = color;
            }

            function searchTable(inputId, tableId) {
                let input = document.getElementById(inputId).value.toLowerCase();
                let table = document.getElementById(tableId);
                let rows = table.getElementsByTagName('tr');

                for (let i = 1; i < rows.length; i++) {
                    let cells = rows[i].getElementsByTagName('td');
                    let match = false;
                    for (let j = 1; j < cells.length - 1; j++) { // Bỏ qua cột STT và Action
                        if (cells[j].innerText.toLowerCase().indexOf(input) > -1) {
                            match = true;
                            break;
                        }
                    }
                    rows[i].style.display = match ? '' : 'none';
                }
            }

            new Chart(document.getElementById('customerChart'), {
                type: 'bar',
                data: {
                    labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
                    datasets: [{
                            label: 'Orders',
                            data: [20, 30, 40, 50, 60, 70, 65],
                            backgroundColor: 'rgba(54, 162, 235, 0.7)',
                            borderColor: 'rgba(54, 162, 235, 1)',
                            borderWidth: 1
                        }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {display: false},
                        tooltip: {enabled: true}
                    },
                    scales: {
                        y: {beginAtZero: true, title: {display: true, text: 'Số đơn hàng'}}
                    }
                }
            });

            new Chart(document.getElementById('restaurantChart'), {
                type: 'bar',
                data: {
                    labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
                    datasets: [{
                            label: 'Revenue',
                            data: [5, 7, 8, 6, 7, 9, 8],
                            backgroundColor: 'rgba(255, 99, 132, 0.7)',
                            borderColor: 'rgba(255, 99, 132, 1)',
                            borderWidth: 1
                        }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {display: false},
                        tooltip: {enabled: true}
                    },
                    scales: {
                        y: {beginAtZero: true, title: {display: true, text: 'Doanh thu (triệu VNĐ)'}}
                    }
                }
            });

            new Chart(document.getElementById('shipperChart'), {
                type: 'bar',
                data: {
                    labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
                    datasets: [{
                            label: 'Deliveries',
                            data: [10, 15, 20, 25, 30, 35, 40],
                            backgroundColor: 'rgba(75, 192, 192, 0.7)',
                            borderColor: 'rgba(75, 192, 192, 1)',
                            borderWidth: 1
                        }]
                },
                options: {
                    responsive: true,
                    plugins: {
                        legend: {display: false},
                        tooltip: {enabled: true}
                    },
                    scales: {
                        y: {beginAtZero: true, title: {display: true, text: 'Số đơn giao'}}
                    }
                }
            });
        </script>

        <%!
            private String getStatusText(int statusId) {
                switch (statusId) {
                    case 1:
                        return "Active";
                    case 0:
                        return "Pending";
                    case 3:
                        return "Banned";
                    default:
                        return "Unknown";
                }
            }
        %>

    </body>
</html>