
import { useState } from "react";
import ReactMarkdown from "react-markdown";

function App() {
  const [input, setInput] = useState<string>("");
  const [response, setResponse] = useState<string>("");
  const [loading, setLoading] = useState<boolean>(false);
  
  const sendRequest = async (): Promise<void> => {
    if(!input.trim()) return;

    console.log("Input value:", input); // ✅ check raw value
    console.log("Payload:", { input }); // ✅ check JSON being sent
    setLoading(true);
    setResponse("");
    
    try{
      
      const res = await fetch("http://localhost:8080/chat", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({input}),
      });
      
      const data= await res.json();
      setResponse(data.reply);
     } catch (err: unknown) {
      if (err instanceof Error) {
        setResponse("Error: " + err.message);
      } else {
        setResponse("Unknown error occurred");
      }
    } finally {
      setLoading(false);
    }
  };

return (
  <div
    style={{
      minHeight: "100vh",
      backgroundColor: "#0f172a",
      padding: "40px 0",
    }}
  >
    <div
      style={{
        maxWidth: "650px",
        margin: "0 auto",
        fontFamily: "Segoe UI, sans-serif",
        color: "#e2e8f0",
      }}
    >
      <h2 style={{ color: "#38bdf8", marginBottom: "20px" }}>
        Blender AI Helper
      </h2>

      <textarea
        value={input}
        onChange={(e: React.ChangeEvent<HTMLTextAreaElement>) =>
          setInput(e.target.value)
        }
        placeholder="Ask your Blender question..."
        style={{
          width: "100%",
          height: "100px",
          padding: "12px",
          borderRadius: "10px",
          border: "1px solid #334155",
          backgroundColor: "#1e293b",
          color: "#fff",
          outline: "none",
          marginBottom: "12px",
        }}
      />

      {/* Button + Spinner */}
      <div
        style={{
          display: "flex",
          alignItems: "center",
          gap: "12px",
        }}
      >
        <button
          onClick={sendRequest}
          disabled={loading}
          style={{
            padding: "10px 18px",
            borderRadius: "8px",
            border: "none",
            backgroundColor: loading ? "#64748b" : "#38bdf8",
            color: "#0f172a",
            fontWeight: 600,
            cursor: loading ? "not-allowed" : "pointer",
          }}
        >
          {loading ? "Thinking..." : "Submit"}
        </button>

        {loading && (
          <div
            style={{
              width: "18px",
              height: "18px",
              border: "2px solid #334155",
              borderTop: "2px solid #38bdf8",
              borderRadius: "50%",
              animation: "spin 1s linear infinite",
            }}
          />
        )}
      </div>

      {/* Response Box */}
      <div
        style={{
          marginTop: "20px",
          border: "1px solid #334155",
          padding: "16px",
          borderRadius: "12px",
          backgroundColor: "#1e293b",
          minHeight: "120px",
          lineHeight: "1.5",
          color: response.startsWith("Error")
            ? "#f87171"
            : "#e2e8f0",
        }}
      >
        <ReactMarkdown
          components={{
            p: ({ ...props }) => (
              <p style={{ margin: "6px 0" }} {...props} />
            ),
            ol: ({ ...props }) => (
              <ol
                style={{
                  paddingLeft: "20px",
                  margin: "6px 0",
                }}
                {...props}
              />
            ),
            li: ({ ...props }) => (
              <li style={{ margin: "4px 0" }} {...props} />
            ),
            strong: ({ ...props }) => (
              <strong
                style={{ color: "#38bdf8" }}
                {...props}
              />
            ),
            code: ({ ...props }) => (
              <code
                style={{
                  backgroundColor: "#334155",
                  padding: "2px 6px",
                  borderRadius: "6px",
                  color: "#facc15",
                }}
                {...props}
              />
            ),
          }}
        >
          {response || "Ask something about Blender..."}
        </ReactMarkdown>
      </div>

      <style>
        {`
          @keyframes spin {
            from {
              transform: rotate(0deg);
            }
            to {
              transform: rotate(360deg);
            }
          }
        `}
      </style>
    </div>
  </div>
);
}
export default App
