<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Customers, model.Shippers, model.Restaurants" %>
<!DOCTYPE html>
<html lang="vi">
    <head>
        <meta charset="UTF-8">
        <title>Admin Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons/font/bootstrap-icons.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <style>
            body {
                background: #f5f5f5;
            }
            .sidebar {
                background: #343a40;
                color: #fff;
                height: 100vh;
                position: fixed;
                width: 220px;
            }
            .sidebar .nav-link {
                color: #adb5bd;
            }
            .sidebar .nav-link:hover {
                color: #fff;
                background: #495057;
                border-radius: 5px;
            }
            .main-content {
                margin-left: 230px;
                padding: 20px;
            }
            .chart-card, .table-container {
                background: #fff;
                border-radius: 10px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.1);
                padding: 20px;
                margin-bottom: 20px;
            }
            .status-online {
                color: green;
                font-weight: bold;
            }
            .status-offline {
                color: gray;
                font-weight: bold;
            }
            .status-active {
                color: green;
                font-weight: bold;
            }
            .status-pending {
                color: orange;
                font-weight: bold;
            }
            .status-banned {
                color: red;
                font-weight: bold;
            }
            .topbar {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 1rem;
            }
            .search-admin {
                width: 300px;
            }
        </style>
    </head>
    <body>
        <div class="container-fluid">
            <div class="row">
                <nav class="col-md-2 sidebar p-3">
                    <div class="text-center mb-4">
                        <i class="bi bi-person-circle" style="font-size: 4rem;"></i>
                        <h4>Admin</h4>
                    </div>
                    <ul class="nav flex-column">
                        <li class="nav-item"><a class="nav-link active" href="admin"><i class="bi bi-speedometer2"></i> Dashboard</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="bi bi-people"></i> Manage Customers</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="bi bi-truck"></i> Manage Shippers</a></li>
                        <li class="nav-item"><a class="nav-link" href="#"><i class="bi bi-shop"></i> Manage Restaurants</a></li>
                        <li class="nav-item"><a class="nav-link" href="manage-payments"><i class="bi bi-wallet2"></i> Manage Payments</a></li>
                        <li class="nav-item"><a class="nav-link" href="pending-accounts"><i class="bi bi-clock-history"></i> Pending Approvals</a></li>
                        <li class="nav-item"><a class="nav-link" href="logout"><i class="bi bi-box-arrow-left"></i> Logout</a></li>
                    </ul>
                </nav>

                <main class="col-md-10 main-content">
                    <div class="topbar">
                        <h2 class="mb-4"><i class="bi bi-speedometer2"></i> Dashboard</h2>
                        <div>
                            <input type="text" class="form-control search-admin d-inline" placeholder="Tìm kiếm chung">
                            <i class="bi bi-bell fs-4 ms-3"></i>
                        </div>
                    </div>

                    <div class="row mb-4">
                        <div class="col-md-4 chart-card">
                            <h6 class="text-center">Số đơn hàng (Customer)</h6>
                            <canvas id="customerChart"></canvas>
                        </div>
                        <div class="col-md-4 chart-card">
                            <h6 class="text-center">Doanh thu (Restaurant)</h6>
                            <canvas id="restaurantChart"></canvas>
                        </div>
                        <div class="col-md-4 chart-card">
                            <h6 class="text-center">Số đơn giao (Shipper)</h6>
                            <canvas id="shipperChart"></canvas>
                        </div>
                    </div>

                    <!-- Customer Table -->
                    <div class="table-container">
                        <h4><i class="bi bi-people"></i> Customer Accounts</h4>
                        <div class="search-box mb-2">
                            <input type="text" class="form-control" placeholder="Search customers">
                        </div>
                        <table class="table table-bordered">
                            <thead class="table-light">
                                <tr><th>STT</th><th>Name</th><th>Email</th><th>Phone</th><th>Status</th><th>Activity</th><th>Action</th></tr>
                            </thead>
                            <tbody>
                                <% int i = 1;
                        List<Customers> customers = (List<Customers>) request.getAttribute("customers");
                        for (Customers c : customers) {%>
                                <tr>
                                    <td><%= i++%></td>
                                    <td><%= c.getName()%></td>
                                    <td><%= c.getEmail()%></td>
                                    <td><%= c.getPhone()%></td>
                                    <td class="status-<%= getStatusClass(c.getStatusId())%>"><%= getStatusText(c.getStatusId())%></td>
                                    <td id="activity-<%= c.getCustomerId()%>">Offline</td>
                                    <td>
                                        <select class="form-select" onchange="updateStatus('customer', <%= c.getCustomerId()%>, this.value)">
                                            <option value="">Select Action</option>
                                            <option value="ban">Ban</option>
                                            <option value="unban">Unban</option>
                                            <option value="edit">Edit Info</option>
                                        </select>
                                    </td>
                                </tr>
                                <% }%>
                            </tbody>
                        </table>
                    </div>
                </main>
            </div>
        </div>

        <script>
            function updateStatus(role, id, action) {
                if (!action)
                    return;
                fetch('update-status', {
                    method: 'POST',
                    headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                    body: `role=${role}&id=${id}&action=${action}`
                }).then(response => {
                    if (response.ok)
                        location.reload();
                    else
                        alert('Update failed!');
                });
            }

            setInterval(() => {
                fetch('check-activity')
                        .then(res => res.json())
                        .then(data => {
                            data.forEach(user => {
                                const el = document.getElementById('activity-' + user.id);
                                if (el)
                                    el.innerHTML = user.online ? '<span class="status-online">Online</span>' : '<span class="status-offline">Offline</span>';
                            });
                        });
            }, 5000);

            new Chart(document.getElementById('customerChart'), {
                type: 'bar',
                data: {
                    labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
                    datasets: [{label: 'Orders', data: [20, 30, 40, 50, 60, 70, 65], backgroundColor: 'rgba(54, 162, 235, 0.7)'}]
                },
                options: {responsive: true}
            });
            new Chart(document.getElementById('restaurantChart'), {
                type: 'bar',
                data: {
                    labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
                    datasets: [{label: 'Revenue', data: [5, 7, 8, 6, 7, 9, 8], backgroundColor: 'rgba(255, 99, 132, 0.7)'}]
                },
                options: {responsive: true}
            });
            new Chart(document.getElementById('shipperChart'), {
                type: 'bar',
                data: {
                    labels: ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'],
                    datasets: [{label: 'Deliveries', data: [10, 15, 20, 25, 30, 35, 40], backgroundColor: 'rgba(75, 192, 192, 0.7)'}]
                },
                options: {responsive: true}
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

            private String getStatusClass(int statusId) {
                switch (statusId) {
                    case 1:
                        return "active";
                    case 0:
                        return "pending";
                    case 3:
                        return "banned";
                    default:
                        return "unknown";
                }
            }
        %>
    </body>
</html>
