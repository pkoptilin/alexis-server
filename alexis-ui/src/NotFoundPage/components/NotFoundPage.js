import React from 'react';
import { Link } from 'react-router-dom';
import { Text404, obiTextNotFound, obiTextFound, linkTextWordgroups, linkTextSetup, linkTextStatistics, linkTextProfile } from './../constants/constants';

export default () => (
    <div className="NotFoundPage">
        <div className="NotFoundPage__main">
            <img src="/images/obi.png" alt="obi-van"/>
            <div className="NotFoundPage__info">
                <p className="NotFoundPage__404">{Text404}</p>
                <p className="NotFoundPage__txt404">{obiTextNotFound}</p>
            </div>
            
        </div>
        <p className="NotFoundPage__exist">{obiTextFound}</p>
        <nav>
            <ul className="NotFoundPage__list-links">
                <li><Link to="/wordgroups" className="NotFoundPage__links">{linkTextWordgroups}</Link></li>
                <li><Link to="/setup" className="NotFoundPage__links">{linkTextSetup}</Link></li>
                <li><Link to="/statistics" className="NotFoundPage__links">{linkTextStatistics}</Link></li>
                <li><Link to="/profile" className="NotFoundPage__links">{linkTextProfile}</Link></li>
            </ul>
        </nav>
    </div>
);
