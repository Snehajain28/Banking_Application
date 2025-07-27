import {  useCallback, useState } from "react";
import {  useDropzone } from "react-dropzone";


const FileUploader = ({ fieldChange, mediaUrl }) => {
  const [file, setFile] = useState([]);
  const [fileUrl, setFileUrl] = useState(mediaUrl);

  const convertFileToUrl = (file) => URL.createObjectURL(file)

  const onDrop = useCallback(
    (acceptedFiles) => {
      setFile(acceptedFiles);
      fieldChange(acceptedFiles);
      setFileUrl(convertFileToUrl(acceptedFiles[0]));
    },
    [fieldChange]
  );

  const { getRootProps, getInputProps } = useDropzone({
    onDrop,
    accept: {
      "image/*": [".png", ".jpeg", ".jpg"],
    },
  });
console.log(file);
  return (
    <div
      {...getRootProps()}
      className="flex justify-center flex-col bg-[#101012] rounded-xl cursor-pointer">
      <input {...getInputProps()} className="cursor-pointer" />

      {fileUrl ? (
        <>
          <div className="flex flex-1 justify-center w-full p-5 lg:p-10">
            <img src={fileUrl} alt="" className="h-80 lg:h-[480px] w-full rounded-[24px] object-fit" />
          </div>
          <p className="text-[#5c5c7b] text-center  text-[14px] font-normal leading-[140%] w-full p-4 border-t border-t-[#1f1f22]">Click or drag photo to replace</p>
        </>
      ) : (
        <div className="flex justify-center items-center flex-col p-7 h-80 lg:h-[612px]">
          <img
            src="https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT_XUPrlJPqFS94uiPxXcw_bLVnlRI9X_RFuA&s"
            width={96}
            height={77}
            alt="file upload"
          />

          <h3 className="text-[16px] font-medium leading-[140%] text-[#efefef] mb-2 mt-6">
            Drag photo here
          </h3>
          <p className="text-[#5c5c7b] text-[14px] font-normal leading-[140%] mb-6">SVG, PNG, JPG</p>

          <button  className="h-12 bg-[#1f1f22] px-5 py-3 text-[#ffffff] flex gap-2">
            Select from computer
          </button>
        </div>
      )}
    </div>
  );
};

export default FileUploader;