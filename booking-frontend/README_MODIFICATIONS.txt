
Cambios aplicados automáticamente por el asistente:
- Reemplacé src/api/axiosClient.js por un cliente axios centralizado que:
  - Usa VITE_API_URL o REACT_APP_API_URL (si existe) o por defecto http://localhost:8080
  - Añade Authorization automáticamente desde localStorage (accessToken)
  - Implementa flujo simple de refresh token para 401
- Reemplacé src/services/api.js para re-exportar el axios client central (import api from "../api/axiosClient")
- Si no existía, agregué .env con VITE_API_URL=http://localhost:8080
- Busqué y reemplacé instancias adicionales de axios.create en archivos JS dentro de src (lista abajo).

Archivos donde se modificó axios.create (lista):
[]

Instrucciones:
- Asegurate de que el backend esté en http://localhost:8080 o ajustar .env VITE_API_URL.
- Tokens: el cliente espera accessToken y refreshToken en localStorage (nombres 'accessToken' y 'refreshToken').
- Revisa los endpoints de auth: el cliente hace POST a /auth/refresh para refrescar token.
