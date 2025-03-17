import React, { useContext } from "react";
import { Upload } from "lucide-react";
import LoadingButton from "./LoadingButton";
import DataContext from "../context/DataContext";
import CircularProgress from "./CircularProgress";

const UploadSection = ({ uploadWarning, handleSubmit, fileInputRef, recruitWarning, setRecruitWarning, uploadProgress }) => {

  const { loading, selectedStatus } = useContext(DataContext);

  const condition = selectedStatus === "In progress";

  console.log(selectedStatus);
  

  return (
    <section className="upload-section">
      <div className="upload-box">
        <input
          type="file"
          id="resume-upload"
          ref={fileInputRef}
          multiple
          {...(!condition && { webkitdirectory: "", directory: "" })} // Add only if the condition is false
          onChange={(e) => handleSubmit(e.target.files)}
          className="file-input"
          disabled={condition}
        />
        <label
          htmlFor="resume-upload"
          className={`upload-label ${condition ? "canWeUpload" : ""}`}
          onClick={(e) => condition && e.preventDefault()} // Prevents upload if condition is true
        >
          <Upload className="upload-icon" />
          <span>Drop here or click to upload</span>
          <span className="upload-hint">Supports: PDF, DOC, DOCX</span>
          {recruitWarning && <span style={{ color: 'green' }}>{recruitWarning}</span>}
        </label>
      </div>
      {loading &&
           <LoadingButton/>
        }
      {uploadWarning && <span style={{ color: 'red' }}>{uploadWarning}</span>}
    </section>
  );


};

export default UploadSection;
