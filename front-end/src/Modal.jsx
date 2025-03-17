import React from 'react'
import Styles from "./Modal.module.css"

const Modal = ({ isOpen, onClose, title, children }) => {
    if (!isOpen) return null;
  
    return (
      <div className={Styles.modal_overlay} onClick={onClose}>
        <div className={Styles.modal_content} onClick={(e) => e.stopPropagation()}>
          <div className={Styles.modal_header}>
            <h2>{title}</h2>
            <button className={Styles.close_btn} onClick={onClose}>X</button>
          </div>
          <div className={Styles.modal_body}>
            {children}
          </div>
        </div>
      </div>
    );
  };

export default Modal