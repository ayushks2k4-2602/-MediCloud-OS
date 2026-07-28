import React from 'react';

interface HeaderProps {
  onMenuClick: () => void;
  hospitalName: string;
}

export const Header: React.FC<HeaderProps> = ({ onMenuClick, hospitalName }) => {
  return (
    <header className="header flex flex-between">
      <div className="header-left flex">
        <button 
          className="btn-icon hamburger-btn" 
          onClick={onMenuClick}
          aria-label="Toggle menu"
        >
          ☰
        </button>
        <div className="badge flex flex-center">
          🏥 {hospitalName}
        </div>
      </div>
      
      <div className="header-right flex">
        <div className="badge badge-success flex flex-center">
          ● Connected
        </div>
        <a href="/swagger" className="btn-ghost api-docs-link">
          Swagger Docs
        </a>
      </div>
    </header>
  );
};
