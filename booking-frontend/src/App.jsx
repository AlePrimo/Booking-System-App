<Routes>
  {/* Home con Login/Register */}
  <Route path="/" element={<Home />} />

  {/* Dashboards */}
  <Route
    path="/dashboard-admin"
    element={
      <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
        <DashboardAdmin />
      </ProtectedRoute>
    }
  />

  <Route
    path="/dashboard-customer"
    element={
      <ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]}>
        <DashboardCustomer />
      </ProtectedRoute>
    }
  />

  <Route
    path="/dashboard-provider"
    element={
      <ProtectedRoute allowedRoles={["ROLE_PROVIDER"]}>
        <DashboardProvider />
      </ProtectedRoute>
    }
  />

  {/* Rutas privadas CUSTOMER */}
  <Route
    path="/reservas"
    element={
      <ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]}>
        <Bookings />
      </ProtectedRoute>
    }
  />
  <Route
    path="/notificaciones"
    element={
      <ProtectedRoute allowedRoles={["ROLE_CUSTOMER"]}>
        <Notifications />
      </ProtectedRoute>
    }
  />

  {/* Rutas privadas ADMIN */}
  <Route
    path="/ofertas"
    element={
      <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
        <Offerings />
      </ProtectedRoute>
    }
  />
  <Route
    path="/usuarios"
    element={
      <ProtectedRoute allowedRoles={["ROLE_ADMIN"]}>
        <Users />
      </ProtectedRoute>
    }
  />

  {/* Rutas privadas compartidas */}
  <Route
    path="/payments"
    element={
      <ProtectedRoute allowedRoles={["ROLE_CUSTOMER", "ROLE_ADMIN", "ROLE_PROVIDER"]}>
        <Payments />
      </ProtectedRoute>
    }
  />

  {/* Unauthorized */}
  <Route path="/unauthorized" element={<Unauthorized />} />

  {/* Fallback */}
  <Route path="*" element={<Navigate to="/" />} />
</Routes>
