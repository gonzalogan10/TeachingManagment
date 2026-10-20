import { useEffect, useState } from "react";

const API_BASE = "http://localhost:8080/api";

async function getTeamEmailPreview(teamId) {
  const response = await fetch(
    `${API_BASE}/email-previews/team/${teamId}`,
    { credentials: "include" }
  );

  if (!response.ok) {
    throw new Error("Failed to fetch email preview");
  }

  return response.json();
}

function TeamEmailPreview({ teamId, onClose }) {
  const [preview, setPreview] = useState(null);
  const [error, setError] = useState(null);
  const [toast, setToast] = useState(null);

  useEffect(() => {
    getTeamEmailPreview(teamId)
      .then(setPreview)
      .catch(() => setError("Failed to load email preview"));
  }, [teamId]);

  if (error) return null;
  if (!preview) return null;

  function copyToClipboard(text) {
    navigator.clipboard.writeText(text)
      .then(() => {
        setToast("Copied to clipboard");

        setTimeout(() => { setToast(null); }, 4000);
      })
      .catch(() => {
        setToast("Failed to copy email body");

        setTimeout(() => { setToast(null);}, 4000);
      });
  }

  return (
    <div style={overlayStyle}>
      <div style={modalStyle}>
        <button style={closeBtnStyle} onClick={onClose}>
          ✕
        </button>

        <h3>{preview.team}</h3>

        {preview.emails.map((email, i) => (
          <div
            key={i}
            style={{
              border: "1px solid #ccc",
              padding: "1rem",
              marginBottom: "1.5rem",
              borderRadius: "6px"
            }}
          >

            <div style={{ marginBottom: "0.5rem" }}>
              <strong>{email.studentName} </strong>
              <em>{email.studentEmail}</em>
            </div>

            <strong>Preview</strong>
            <pre style={{ whiteSpace: "pre-wrap"}}>
              {email.body}
            </pre>

            <button onClick={() => copyToClipboard(email.body)}>
               <span className="material-icons">content_copy</span>
            </button>

          </div>
        ))}
      </div>

      {toast && (
        <div style={toastStyle}>
          {toast}
        </div>
      )}
    </div>
  );
}

export default TeamEmailPreview;

const overlayStyle = {
  position: "fixed",
  top: 0,
  left: 0,
  width: "100vw",
  height: "100vh",
  backgroundColor: "rgba(0,0,0,0.5)",
  display: "flex",
  justifyContent: "center",
  alignItems: "center",
  zIndex: 1000
};

const modalStyle = {
  backgroundColor: "white",
  padding: "2rem",
  borderRadius: "8px",
  width: "80%",
  maxHeight: "90vh",
  overflowY: "auto",
  position: "relative"
};

const closeBtnStyle = {
  position: "absolute",
  top: "10px",
  right: "10px",
  background: "none",
  border: "none",
  fontSize: "1.2rem",
  cursor: "pointer"
};

const toastStyle = {
  position: "fixed",
  bottom: "20px",
  left: "50%",
  transform: "translateX(-50%)",
  backgroundColor: "black",
  color: "white",
  padding: "0.75rem 1.5rem",
  borderRadius: "6px",
  fontSize: "0.9rem",
  zIndex: 2000,
  boxShadow: "0 4px 10px rgba(0,0,0,0.3)"
};
