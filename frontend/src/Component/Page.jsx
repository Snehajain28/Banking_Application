import React from 'react';
import { Document, Page } from 'react-pdf/dist/esm/entry.webpack';
import pdfFile from './assets/aadhaar.pdf'; 

function Page2() {

    return (
        <div>
            <Document file={pdfFile}>
                <Page pageNumber={1} />
            </Document>
        </div>
    );
}

export default Page2;