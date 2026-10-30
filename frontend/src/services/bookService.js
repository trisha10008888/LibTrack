
const API_URL = (
  import.meta.env.VITE_API_URL || "http://localhost:8080"
).replace(/\/$/, "") + "/api";

export async function getAllBooks() {
  const response = await fetch(`${API_URL}/books`);
  if (!response.ok) throw new Error("Unable to load books");
  return response.json();
}

export async function getAllRequests() {
  const response = await fetch(`${API_URL}/requests`);
  if (!response.ok) throw new Error("Unable to load requests");
  return response.json();
}

export async function createBookRequest(bookId) {
  const response = await fetch(
    `${API_URL}/requests/book/${bookId}`,
    { method: "POST" }
  );
  if (!response.ok) throw new Error("Unable to request this book");
  return response.json();
}

export async function deleteBookRequest(requestId) {
  const response = await fetch(
    `${API_URL}/requests/${requestId}`,
    { method: "DELETE" }
  );
  if (!response.ok) throw new Error("Unable to cancel request");
}

export async function updateRequestStatus(requestId, status) {
  const response = await fetch(
    `${API_URL}/requests/${requestId}/status?status=${encodeURIComponent(status)}`,
    { method: "PUT" }
  );
  if (!response.ok) throw new Error("Unable to update request");
  return response.json();
}
