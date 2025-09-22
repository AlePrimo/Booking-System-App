import { useEffect, useState } from "react";
import api from "../api";

export default function Offerings() {
  const [offerings, setOfferings] = useState([]);

  useEffect(() => {
    api.get("/offerings").then((res) => setOfferings(res.data.content || res.data));
  }, []);

  return (
    <div className="p-6">
      <h2 className="text-2xl font-bold mb-4">Servicios</h2>
      <ul className="space-y-2">
        {offerings.map((o) => (
          <li key={o.id} className="border p-2 rounded">
            {o.name} - ${o.price}
          </li>
        ))}
      </ul>
    </div>
  );
}

