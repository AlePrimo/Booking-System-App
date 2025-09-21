export default function Home() {
  return (
    <header className="bg-gradient-to-r from-indigo-600 to-purple-600 text-white py-20">
      <div className="container mx-auto px-6 text-center">
        <h1 className="text-5xl font-bold mb-4">
          Bienvenido a Booking System
        </h1>
        <p className="text-lg mb-6">
          Gestiona reservas, pagos y usuarios de manera sencilla.
        </p>
        <button className="bg-white text-indigo-600 px-6 py-3 rounded-lg font-semibold hover:bg-gray-200 transition">
          Empezar ahora
        </button>
      </div>
    </header>
  );
}
