import React, { useEffect, useState, useCallback } from "react";
import axios from "axios";
import { Button, Table, Modal, Form } from "react-bootstrap";
import { useNavigate } from "react-router-dom";

const AdminDashboard = () => {
  const navigate = useNavigate();
  const token = localStorage.getItem("jwtToken");
  const role = localStorage.getItem("role");

  const [products, setProducts] = useState([]);
  const [users, setUsers] = useState([]);
  const [orders, setOrders] = useState([]);
  const [showModal, setShowModal] = useState(false);
  const [newProduct, setNewProduct] = useState({
    name: "",
    description: "",
    brand: "",
    price: "",
    category: "",
    quantity: "",
  });
  const [image, setImage] = useState(null);
  const [loading, setLoading] = useState(false);

  // ===== Role-based Access =====
  useEffect(() => {
    if (!token || role?.toUpperCase() !== "ADMIN") {
      alert("🚫 Access denied. Only admin allowed.");
      navigate("/login");
    }
  }, [navigate, token, role]);

  // ===== Fetch APIs =====
  const fetchProducts = useCallback(async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/products", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setProducts(res.data || []);
    } catch (err) {
      console.error("Error fetching products:", err);
    }
  }, [token]);

  const fetchUsers = useCallback(async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/user/users", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setUsers(res.data || []);
    } catch (err) {
      console.error("Error fetching users:", err);
    }
  }, [token]);

  const fetchOrders = useCallback(async () => {
    try {
      const res = await axios.get("http://localhost:8080/api/orders/all", {
        headers: { Authorization: `Bearer ${token}` },
      });
      setOrders(res.data || []);
    } catch (err) {
      console.error("Error fetching orders:", err);
    }
  }, [token]);

  // Fetch all data on mount
  useEffect(() => {
    if (token && role?.toUpperCase() === "ADMIN") {
      setLoading(true);
      Promise.all([fetchProducts(), fetchUsers(), fetchOrders()]).finally(() =>
        setLoading(false)
      );
    }
  }, [fetchProducts, fetchUsers, fetchOrders, token, role]);

  // ===== Add Product =====
  const handleAddProduct = async (e) => {
    e.preventDefault();
    const formData = new FormData();
    formData.append("product", JSON.stringify(newProduct));
    if (image) formData.append("imageFile", image);

    try {
      await axios.post("http://localhost:8080/api/products/add", formData, {
        headers: {
          Authorization: `Bearer ${token}`,
          "Content-Type": "multipart/form-data",
        },
      });
      setShowModal(false);
      setNewProduct({
        name: "",
        description: "",
        brand: "",
        price: "",
        category: "",
        quantity: "",
      });
      setImage(null);
      fetchProducts();
      alert("✅ Product added successfully!");
    } catch (err) {
      console.error("Error adding product:", err);
      alert("❌ Failed to add product");
    }
  };

  // ===== Delete Product =====
  const deleteProduct = async (id) => {
    if (!window.confirm("Are you sure you want to delete this product?")) return;
    try {
      await axios.delete(`http://localhost:8080/api/products/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchProducts();
      alert("🗑️ Product deleted successfully");
    } catch (err) {
      console.error("Error deleting product:", err);
    }
  };

  // ===== Delete User =====
  const deleteUser = async (id) => {
    if (!window.confirm("Are you sure you want to delete this user?")) return;
    try {
      await axios.delete(`http://localhost:8080/api/admin/deleteUser/${id}`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchUsers();
      alert("🗑️ User deleted successfully");
    } catch (err) {
      console.error("Error deleting user:", err);
    }
  };

  // ===== Update Order Status =====
  const updateOrderStatus = async (orderId, status) => {
    try {
      await axios.put(
        `http://localhost:8080/api/orders/${orderId}/status?status=${status}`,
        {},
        { headers: { Authorization: `Bearer ${token}` } }
      );
      fetchOrders();
      alert("🚚 Order status updated!");
    } catch (err) {
      console.error("Error updating order:", err);
    }
  };

  if (loading) return <h2 className="text-center mt-5">Loading dashboard...</h2>;

  return (
    <div className="container my-5">
      <h1 className="text-center mb-4 fw-bold text-primary">Admin Dashboard</h1>

      {/* ===== Products ===== */}
      <div className="mb-5">
        <div className="d-flex justify-content-between align-items-center mb-3">
          <h3>📦 Manage Products</h3>
          <Button onClick={() => setShowModal(true)}>+ Add Product</Button>
        </div>
        <Table striped bordered hover responsive>
          <thead>
            <tr>
              <th>#</th>
              <th>Image</th>
              <th>Name</th>
              <th>Brand</th>
              <th>Price</th>
              <th>Category</th>
              <th>Qty</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {products.length > 0 ? (
              products.map((p, i) => (
                <tr key={p.id}>
                  <td>{i + 1}</td>
                  <td>
                    <img
                      src={`http://localhost:8080/api/products/image/${p.id}`}
                      alt={p.name}
                      style={{ width: "50px", height: "50px" }}
                    />
                  </td>
                  <td>{p.name}</td>
                  <td>{p.brand}</td>
                  <td>₹{p.price}</td>
                  <td>{p.category}</td>
                  <td>{p.quantity}</td>
                  <td>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => deleteProduct(p.id)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="8" className="text-center">
                  No products found
                </td>
              </tr>
            )}
          </tbody>
        </Table>
      </div>

      {/* ===== Users ===== */}
      <div className="mb-5">
        <h3>👤 Manage Users</h3>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>ID</th>
              <th>Name</th>
              <th>Email</th>
              <th>Phone</th>
              <th>Role</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {users.length > 0 ? (
              users.map((u) => (
                <tr key={u.userId}>
                  <td>{u.userId}</td>
                  <td>{u.userName}</td>
                  <td>{u.userEmail}</td>
                  <td>{u.userPhoneNo}</td>
                  <td>{u.roles?.map((r) => r.name).join(", ") || "N/A"}</td>
                  <td>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => deleteUser(u.userId)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="text-center">
                  No users found
                </td>
              </tr>
            )}
          </tbody>
        </Table>
      </div>

      {/* ===== Orders ===== */}
      <div>
        <h3>📑 Manage Orders</h3>
        <Table striped bordered hover>
          <thead>
            <tr>
              <th>ID</th>
              <th>User</th>
              <th>Amount</th>
              <th>Status</th>
              <th>Date</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {orders.length > 0 ? (
              orders.map((o) => (
                <tr key={o.orderId}>
                  <td>{o.orderId}</td>
                  <td>{o.user?.userName || "N/A"}</td>
                  <td>₹{o.totalAmount}</td>
                  <td>{o.status}</td>
                  <td>{new Date(o.orderDate).toLocaleString()}</td>
                  <td>
                    {o.status !== "DELIVERED" && (
                      <Button
                        variant="success"
                        size="sm"
                        onClick={() =>
                          updateOrderStatus(o.orderId, "DELIVERED")
                        }
                      >
                        Mark Delivered
                      </Button>
                    )}
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan="6" className="text-center">
                  No orders found
                </td>
              </tr>
            )}
          </tbody>
        </Table>
      </div>

      {/* ===== Add Product Modal ===== */}
      <Modal show={showModal} onHide={() => setShowModal(false)} centered>
        <Modal.Header closeButton>
          <Modal.Title>Add New Product</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form onSubmit={handleAddProduct}>
            {["name", "brand", "price", "category", "quantity", "description"].map(
              (field) => (
                <Form.Group className="mb-3" key={field}>
                  <Form.Label>
                    {field.charAt(0).toUpperCase() + field.slice(1)}
                  </Form.Label>
                  <Form.Control
                    type={field === "price" || field === "quantity" ? "number" : "text"}
                    as={field === "description" ? "textarea" : undefined}
                    rows={field === "description" ? 3 : undefined}
                    name={field}
                    required={field !== "description"}
                    value={newProduct[field]}
                    onChange={(e) =>
                      setNewProduct({ ...newProduct, [field]: e.target.value })
                    }
                  />
                </Form.Group>
              )
            )}
            <Form.Group className="mb-3">
              <Form.Label>Image</Form.Label>
              <Form.Control
                type="file"
                onChange={(e) => setImage(e.target.files[0])}
              />
            </Form.Group>
            <Button type="submit" variant="primary" className="w-100">
              Add Product
            </Button>
          </Form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default AdminDashboard;
