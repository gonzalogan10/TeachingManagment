import { useState } from "react";
import "./ComponentsStyle.css";

function CriterionFilter(props) {
    // props: criteria (array), onFilterChange (callback with filtered criteria)
    const [filterCategory, setFilterCategory] = useState("");
    const [filterName, setFilterName] = useState("");


    // Updates the filter when category or name changes. Calls parent callback.
    function updateFilter(newCategory, newName) {
        const categories = [];
        for (let i = 0; i < props.criteria.length; i++) {
            const c = props.criteria[i].category || "";
            if (c === "") 
                continue;
            let exists = false;
            for (let j = 0; j < categories.length; j++) {
                if (categories[j] === c) {
                    exists = true; break;
                }
            }
            if (!exists) { 
                categories.push(c);
            }
        }

        // Apply filters
        const filtered = [];
        for (let i = 0; i < props.criteria.length; i++) {
            const crit = props.criteria[i];
            if (newCategory && crit.category !== newCategory) { continue; }
            const name = (crit.name || "").toLowerCase();
            const query = newName.toLowerCase();
            if (query && name.indexOf(query) === -1) { continue; }
            filtered.push(crit);
        }

        // Call parent callback (pattern to call a function passed as prop)
        if (typeof props.onFilterChange === 'function') {
            props.onFilterChange(filtered);
        }
    }

    function handleCategoryChange(e) {
        const cat = e.target.value;
        setFilterCategory(cat);
        updateFilter(cat, filterName);
    }

    function handleNameChange(e) {
        const name = e.target.value;
        setFilterName(name);
        updateFilter(filterCategory, name);
    }

    // Build category options. Render means creating React elements.
    function renderCategoryOptions() {
        const categories = [];
        for (let i = 0; i < props.criteria.length; i++) {
            const c = props.criteria[i].category || "";
            if (c === "") continue;
            let exists = false;
            for (let j = 0; j < categories.length; j++) {
                if (categories[j] === c) { exists = true; break; }
            }
            if (!exists) { categories.push(c); }
        }
        const options = [<option key="all" value="">All categories</option>];
        for (let k = 0; k < categories.length; k++) {
            options.push(<option key={categories[k]} value={categories[k]}>{categories[k]}</option>);
        }
        return options;
    }

    return (
        <div className="criterion-filter">
            <div className="filter-row">
                <label>Category: </label>
                <select value={filterCategory} onChange={handleCategoryChange}>
                    {renderCategoryOptions()}
                </select>
            </div>
            <div className="filter-row">
                <label>Name contains: </label>
                <input type="text" value={filterName} onChange={handleNameChange} />
            </div>
        </div>
    );
}

export default CriterionFilter;
